package starbug.model.dto.resoures;

import lombok.*;
import starbug.model.entity.inventory.InventoryLogEntity;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.entity.parcel.ProducerEntity;
import starbug.model.entity.resoures.RecipeEntity;
import starbug.model.entity.resoures.RescategoryEntity;
import starbug.model.entity.resoures.ResouresEntity;
import starbug.model.entity.product.ProductEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
// 재료 카테고리 , 거래처 , 재료 , 레시피  , 발주로그 , 재고로그 합침
public class ResouresDto {

    // 재료 카테고리 필드
    private int rescno;         //카테고리 번호

    private String rescname;    //카테고리 이름

    // 재료 필드
    private int resno; // 재료번호
    private String resname; // 재료 이름
    private int resprice; // 재료 가격
    private double rescount; // 재료 개수
    //private int prno; // 거래처 번호fk
    //private int rescno; //카테고리 번호fk

    // 거래처 필드
    private int prno; // 거래처 번호
    private String prname; // 거래처이름


    //레시피 필드
    private int rno; // 레시피 식별번호
    private double rquantity; // 재료 필요 수량
    private int pno; // 상품번호 fk
    //private int resno; // 재료번호 fk

    // 재고 로그
    private  int inlogno ;                   // 재료 로그 번호
    private LocalDateTime inlogdate;         // 로그 날짜
    private  double inloghistory ;           // 로그 입출내역
    private  double salecount ;              // 판매개수

    //발주로그
    private int pano; // 발주 식별 번호
    private int pacount; // 주문 개수
    private boolean pastate; // 발주 상태

    private LocalDateTime cdate; // 주문 시간
    private LocalDateTime udate; // 도착시간

    // 재료 카테고리
    public RescategoryEntity toRescategoryEntity() {

        return  RescategoryEntity.builder()
                .rescno(this.rescno)
                .rescname(this.resname)
                .build();
    }

    //거래저
    public ProducerEntity toProducerEntity() {

        return ProducerEntity.builder()
                .prno(this.prno)
                .prname(this.prname)
                .build();
    }

    //재료
    public ResouresEntity toResouresEntity() {

        return ResouresEntity.builder()
               .resno(this.resno)
               .resname(this.resname)
               .resprice(this.resprice)

               .build();
    }

    // 재고로그
    public InventoryLogEntity toInventoryLogEntity() {

        return InventoryLogEntity.builder()
              .inlogno(this.inlogno)
              .inlogdate(this.inlogdate)
              .inloghistory(this.inloghistory)
              .resouresEntity(this.toResouresEntity())
              .build();
    }

    //레시피
    public RecipeEntity toRecipeEntity() {

        return RecipeEntity.builder()
                .rno(this.rno)
                .rquantity(this.rquantity)
                .build();
    }

    // 발주로그
    public ParcellogEntity toParcellogEntity() {

        return ParcellogEntity.builder()
                .pano(this.pano)
                .pacount(this.pacount)
                .pastate(this.pastate)
                .resouresEntity(this.toResouresEntity())
                .build();
    }


}
