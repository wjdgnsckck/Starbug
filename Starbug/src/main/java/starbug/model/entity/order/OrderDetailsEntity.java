package starbug.model.entity.order;

import lombok.*;
import starbug.model.dto.order.OrdersDto;
import starbug.model.entity.product.ProductEntity;

import javax.persistence.*;

@Entity
@Table(name = "orders_details")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString @Builder
public class OrderDetailsEntity { // 주문 상세 테이블

    // 주문상세번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int odtno;

    // 주문번호 FK
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ono")
    private OrdersEntity ordersEntity;

    // 상품번호 FK
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "pno")
    private ProductEntity productEntity;

    // 변환
    public OrdersDto toOrdersDto(){
        return OrdersDto.builder()
                .odtno(this.odtno)
                .ono(this.ordersEntity.getOno())
                .pno(this.productEntity.getPno())
                .build();

    }


}
