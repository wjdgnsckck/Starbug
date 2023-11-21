package starbug.model.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.member.CouponHistoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CouponHistoryEntityRepository extends JpaRepository <CouponHistoryEntity , Integer> {

    // 예지 : 회원에 할당된 쿠폰 호출하기 위해 작성
    List<CouponHistoryEntity> findAllByStarBugMemberEntity_Mno(int mno );
    @Query(value = "select ch.chisno, c.ccno, ch.chstate, c.cccontent, c.ccpercent, ch.mno from coupon_history ch" +
            " natural join coupon c " +
            "    where mno = :mno"
            , nativeQuery = true )
    List<Map<String , Object>> findByMemberCoupon( int mno );


    @Query(value = "select * from coupon_history natural join coupon c where chisno = :chisno"
            , nativeQuery = true )
    Map<String, Object> findByChisno ( int chisno);

    @Query(value ="select * from coupon_history where mno=:mno and chisno =:chisno" , nativeQuery = true )
    Optional<CouponHistoryEntity> findByMnoAndCoupon(int mno,int chisno);

    //Optional<CouponHistoryEntity> findById( int ccno );

    @Query(value ="select * from coupon_history where chstate =:chstate" , nativeQuery = true )
    Optional<CouponHistoryEntity> findByOrder( int chstate );

    // --------------------------------------------------------------------
    // 미사용인데 쿠폰 사용기한 지난 쿠폰들 찾기
    @Query(value ="select * from coupon_history as ch " +
            " inner join coupon as cc on ch.ccno = cc.ccno " +
            " where ch.chstate =:chstate " +
            " and date_add(ch.cdate , interval cc.ccdate day) < now() " //date_add 함수 사용하여 날짜계산
            , nativeQuery = true )
    List<CouponHistoryEntity> findByStateCoupon(int chstate);

    // 회원번호로 쿠폰히스토리 검색
   // List<CouponHistoryEntity> findBymemberMnoEntity( int mno );
}
