package starbug.model.repository.resoures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.entity.product.ProductEntity;
import starbug.model.entity.resoures.RecipeEntity;
import starbug.model.entity.resoures.ResouresEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface ResouresEntityRepository
        extends JpaRepository<ResouresEntity, Integer> {



        //검색기능 쿼리문
        @Query( value = " select * from resources where " +
                " if(  :keyword = '' , true , " +
                " if(  :keyword !='' , resname like %:keyword%, true ) )" // String keyword가 없을때,keyword가 resname 이랑같을때 비교하기
                , nativeQuery = true )
        Page<ResouresEntity> findBySearch( String keyword , Pageable pageable );



        //재고 이름에 맞는 값 출력 검색에서 사용함
        ResouresEntity findByResname(String resname);

        //재고개수를 확인해주는 쿼리문 !! 발주개수 확인한다.
        @Query(value = "select sum(parcel_log.pcount) from resources natural" +
                " join parcel_log where resno =:resno",nativeQuery = true)

         Double findBypcount (int resno);
        //재고개수를 확인해주는 쿼리문 !! 판매개수 확인한다.

        @Query(value = "select sum(recipe.rquantity) from recipe INNER JOIN orders_details " +
                "where recipe.pno=orders_details.pno and recipe.resno=:resno",nativeQuery = true)
         Double findByrquantity(int resno);

        // 예지 작성 : 매입 출력 기능 구현을 위해 필요 ------------------------------
        @Query(value = "select SUM(r.resprice * p.pcount) AS total_value  " +
                " from parcel_log p " +
                " natural join resources r " +
                " where p.cdate like %:cdate% "
                , nativeQuery = true )
        int findByPacelLogForPaid( String cdate );

        // 예지 작성 : 손익계산서 ------------------------------
        @Query(value = "select SUM(r.resprice * p.pcount) AS totalPurchase" +
                " from parcel_log p " +
                "    natural join resources r " +
                "    where cdate BETWEEN :startDate AND :endDate"
                , nativeQuery = true )
        Integer findByIncomePurchase(String startDate, String endDate  );


        // 민재작성 재료카테고리 , 재료 , 거래처 조인하기
        // 재료 테이블의 fk 카테고리 번호와 선택한 카테고리 번호 일치하는 것만 출력
        @Query( value = "select * from resources " +
                        "natural join rescategory rc " +
                        "natural join producer where rc.rescno = :rescno order by resno asc" ,
                        nativeQuery = true)
        List<ResouresEntity> findAllByResourcesAndRescategoryAndProducerAscResno( int rescno );





}
