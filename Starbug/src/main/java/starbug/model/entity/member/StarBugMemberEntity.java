package starbug.model.entity.member;

import lombok.*;
import starbug.model.dto.member.StarBugMemberDto;
import starbug.model.entity.order.OrdersEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
public class StarBugMemberEntity extends BaseTimeMember { // 회원 테이블

    // 회원번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;

    // 회원이름
    @Column(name = "mname", nullable = false)
    private String mname;

    // 회원전화번호
    @Column(name = "mphone", nullable = false)
    private String mphone;

    // 회원나이
    @Column(name = "mage", nullable = false)
    private int mage;

    // 회원성별
    @Column(name = "msex", nullable = false)
    private String msex;



    // 회원추가정보 (메모)
    @Column(name = "metc", nullable = false)
    private String metc;

    @Builder.Default // 쿠폰내역들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "starBugMemberEntity")
    private List<CouponHistoryEntity> couponHistoryEntities = new ArrayList<>();

    @Builder.Default // 주문내역들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "starBugMemberEntity")
    private List<OrdersEntity> ordersEntities = new ArrayList<>();




    // 회원 등록, 수정, 삭제를 위한 dto
    public StarBugMemberDto memberToDto(){
        return StarBugMemberDto.builder()
                .mno(this.mno)
                .mname(this.mname)
                .mphone(this.mphone)
                .mage(this.mage)
                .msex(this.msex)
                .mdate(this.getMdate())
                .metc(this.metc)
                .build();
    }

}