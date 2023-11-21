package starbug.model.entity.member;

import lombok.*;
import starbug.model.dto.member.CouponHistoryDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "coupon")
public class CouponEntity { // 쿠폰 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int ccno ;         // 쿠폰 번호 pk
    @Column(name = "ccpercent",nullable = false )
    private  int ccpercent ;    //  쿠폰 할인율
    @Column(name = "cccontent",nullable = false ,unique = true)
    private  String cccontent ; //  쿠폰 내용
    @Column(name = "ccdate",nullable = false )
    private  int ccdate ;       // 쿠폰 사용기한일

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "couponEntity", cascade = CascadeType.ALL)
    private List<CouponHistoryEntity> couponHistoryEntities = new ArrayList<>();

    public CouponHistoryDto couponToDto(){
        return CouponHistoryDto.builder()
                .ccno(this.ccno)
                .ccpercent(this.ccpercent)
                .cccontent(this.cccontent)
                .ccdate(this.ccdate)
                .build();
    }

}
