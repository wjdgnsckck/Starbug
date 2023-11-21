package starbug.model.entity.product;

import lombok.*;
import starbug.model.dto.product.ProductDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_category")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class ProductCategoryEntity { // 상품 카테고리 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcno; // 상품 카테고리 식별번호

    @Column(length = 50, nullable = false, unique = true)
    private String pcname; // 상품 카테고리 이름

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productCategoryEntity", cascade = CascadeType.ALL)
    private List<ProductEntity> productEntities = new ArrayList<>();

    public ProductDto toProductDto() {
        return ProductDto.builder()
                .pcno(this.pcno)
                .pcname(this.pcname)
                .build();
    }
}
