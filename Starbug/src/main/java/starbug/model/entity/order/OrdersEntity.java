package starbug.model.entity.order;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import starbug.model.dto.order.OrdersDto;
import starbug.model.entity.member.StarBugMemberEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
//@MappedSuperclass
//@EntityListeners( AuditingEntityListener.class)
public class OrdersEntity extends BaseTimeForOrder { // 주문테이블

    // 주문번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ono;

    // 주문날짜
//    @LastModifiedDate
//    private LocalDateTime odate;

    // 주문상태 | 0:결제완료(판매), 1:환불
    @Column(name = "ostate", nullable = false)
    @ColumnDefault("0")
    private int ostate;

    // 총 주문금액
    @Column(name = "totalprice", nullable = false)
    private int totalprice;

    // 총 결제금액
    @Column(name = "paid", nullable = false)
    private int paid;

//    @Column(name = "mno", nullable = false)
//    private int mno;
    // 회원번호 FK
    @ToString.Exclude
    @JoinColumn(name = "mno")
    @ManyToOne
    private StarBugMemberEntity starBugMemberEntity;


    @Builder.Default // 주문상세들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordersEntity", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetailsEntities = new ArrayList<>();


    // 변환
    public OrdersDto toOrdersDto(){
        return OrdersDto.builder()
                .ono(this.ono)
                .mno(this.starBugMemberEntity.getMno()) //샘플 데이터를 단방향으로 저장 해 둔것이 있어서 양방향 호출이 안되는 데이터가 있어서 오류 뜸
                //.mname(this.starBugMemberEntity.getMname())
                .odate(this.getOdate())
                .ostate(this.ostate)
                .totalprice(this.totalprice)
                .paid(this.paid)
                .build();
    }

}