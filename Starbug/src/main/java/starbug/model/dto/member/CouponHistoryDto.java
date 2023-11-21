package starbug.model.dto.member;

import lombok.*;
import starbug.model.entity.member.CouponEntity;
import starbug.model.entity.member.CouponHistoryEntity;
import starbug.model.entity.member.StarBugMemberEntity;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class CouponHistoryDto {
    private int ccno;   // 쿠폰번호
    private int ccpercent;  // 쿠폰 할인율
    private String cccontent;    // 쿠폰 내용
    private int ccdate;     // 쿠폰 사용 기한일

    private int chisno; // 쿠폰관련 로그 순번
    private String chcode;  //쿠폰코드
    private int chstate;    // 쿠폰상태

    private int mno;
    // + BaseTime
    private LocalDateTime cdate;
    private LocalDateTime udate;


    // 쿠폰카테고리
    public CouponEntity couponToEntity(){
        return CouponEntity.builder()
                .ccno(this.ccno)
                .ccpercent(this.ccpercent)
                .cccontent(this.cccontent)
                .ccdate(this.ccdate)
                .build();
    }

    // 쿠폰로그
    public CouponHistoryEntity couponHistoryToEntity(){
        return CouponHistoryEntity.builder()
                .chisno(this.chisno)
                .chcode(this.chcode)
                .chstate(this.chstate)
                .build();
    }


}
