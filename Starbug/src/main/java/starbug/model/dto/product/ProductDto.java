package starbug.model.dto.product;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.product.ProductCategoryEntity;
import starbug.model.entity.product.ProductEntity;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class ProductDto {
    private int pno; // 상품 식별번호
    private String pname; // 상품 이름
    private int pprice; // 상품 가격
    private int pcno; // 상품 카테고리 식별번호
    private String pcname; // 상품 카테고리 이름
    private List<ResouresDto> resnos; // 재료들
    private int pevent; // 이벤트 여부


    public ProductEntity toProductEntity() {
        return ProductEntity
                .builder()
                .pno(this.pno)
                .pname(this.pname)
                .pprice(this.pprice)
                .pevent(this.pevent)
                .build();
    }

    public ProductCategoryEntity toProductCategoryEntity() {
        return ProductCategoryEntity
                .builder()
                .pcno(this.pcno)
                .pcname(this.pcname)
                .build();
    }
}
