package starbug.model.repository.resoures;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.parcel.ParcellogEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParcellogEntityRepository extends JpaRepository<ParcellogEntity, Integer> {

    List<ParcellogEntity> findAllByUdate( String udate );


    //---------------------정훈 재고 개수 로직을 위한 코드-------------------------//
   // ParcellogEntity findByResno( ResouresEntity resouresEntity );

    //--------------------민재작성 쿼리---------------------------------------------------//
    // 발주 내역 출력하는 쿼리문 발주내역 , 재료 join 하고 검색한 년 월 일 (시간X) 해당하는 주문만 출력
    @Query(value = "select pl.pano , pl.cdate , pl.udate , pl.pcount , pl.resno , pl.pastate ,"
            +" res.resname , resprice"
            +" from parcel_log pl natural join resources res where cdate like %:cdate%"
            , nativeQuery = true )
    List<ParcellogEntity> findByPacelLogAndResoures( String cdate );

    // cdate 중  검색한 년 월 일 을 포함하고 cdate 와 udate 가 일치한 (도착처리 안된) 주문만 출력 
    @Query(value = "select * from parcel_log " +
            "where cdate like %:localDate% and cdate = udate"
            , nativeQuery = true )
    Optional<ParcellogEntity> findByCdateEqualsUdate(LocalDate localDate);

    // cdate 중 검색한 년 월 일 포함 , 도착 여부는 제외
    @Query(value = "select * from parcel_log " +
            "where cdate like %:localDate%"
            , nativeQuery = true )
    List<ParcellogEntity> findByCdate(LocalDate localDate);

    // 2023년에 해당하는 주문중 cdte 의 중복 된 날짜 제거해서 select
    @Query(value = "select distinct cdate , pano , udate , pcount , resno , pastate from parcel_log where cdate like %:cyear% order by cdate asc" ,
            nativeQuery = true )
    List<ParcellogEntity> findByCdateDistinctCdate(String cyear);
    
    // parcellog 의 pastate 가 true 인것만 삭제
    @Query(value = "delete from parcel_log where pastate = :palogstate" ,
            nativeQuery = true )
    void deleteAllByPastate(boolean palogstate );

    @Query( value ="select pano , max(cdate) as cdate , udate , pcount , resno , pastate  from parcel_log" , nativeQuery = true )
    Optional<ParcellogEntity> findGetMaxCdate();

    List<ParcellogEntity> findByPastate(boolean pastate);
    //----------------------------------------------------------------------------------//
    //----------------------- 예지 작성 ------------------------------------------//
    @Query(value = "select pl.*, r.resprice " +
                    " from parcel_log pl " +
                    " natural join resources r where " +
                    " if( :keyword = '', true, " +                            // 전체 검색
                    " if( :key = 'resname', resname like %:keyword% , " +     // 발주품목 검색
                    " if( :key = 'cdate', cdate like %:keyword% , true )))" +
                    " order by pl.cdate desc"   // 발주날짜 검색
            , nativeQuery = true )
    Page<ParcellogEntity> findByPurchaseListAll(String key, String keyword, Pageable pageable);





}

