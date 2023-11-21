package starbug.model.dto.order;


import lombok.*;
import starbug.model.entity.member.StarBugMemberEntity;
import starbug.model.entity.order.OrderDetailsEntity;
import starbug.model.entity.order.OrdersEntity;
import starbug.model.entity.product.ProductEntity;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString @Builder
public class OrdersDto {

    private int ono;


    private int mno;
    private String mname;
    private LocalDateTime odate;
    private int ostate;
    private int totalprice;
    private int paid;


    private int odtno;
    private int pno;
    private String pname;

    // 변환
    public OrdersEntity toOrders(){
        StarBugMemberEntity memberEntity = new StarBugMemberEntity();
        memberEntity.setMno(this.mno);

        return OrdersEntity.builder()
                .ono(this.ono)
                .starBugMemberEntity(memberEntity)
                .ostate(this.ostate)
                .totalprice(this.totalprice)
                .paid(this.paid)
                .build();
    }
    public OrderDetailsEntity toOrderDetails(){

        return OrderDetailsEntity.builder()
                .odtno(this.odtno)
                .build();
    }



}
