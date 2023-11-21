package starbug.model.entity.member;

import lombok.*;
import starbug.model.dto.member.CouponHistoryDto;
import starbug.model.entity.BaseTime;

import javax.persistence.*;

@Entity
@Table(name = "coupon_history")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
public class CouponHistoryEntity extends BaseTime { // 쿠폰 사용로그 테이블

    // 쿠폰관련 로그 순번
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chisno;

    // 쿠폰코드
    @Column(name = "chcode", nullable = false)
    private String chcode;

    // 쿠폰상태
    @Column(name = "chstate", nullable = false)
    private int chstate;



    // 쿠폰 할인율 식별번호
    @ToString.Exclude
    @JoinColumn(name = "ccno")
    @ManyToOne
    private CouponEntity couponEntity;

    // 쿠폰 소유한 회원번호
    @ToString.Exclude
    @JoinColumn(name = "mno")
    @ManyToOne
    private StarBugMemberEntity starBugMemberEntity;

    public CouponHistoryDto couponHistoryToDto(){
        return CouponHistoryDto.builder()
                .chisno(this.chisno)
                .chcode(this.chcode)
                .chstate(this.chstate)
                .ccno(this.couponEntity.getCcno())
                .cdate(this.getCdate())
                .udate(this.getUdate())
                .mno(this.starBugMemberEntity.getMno())
                .build();
    }



}