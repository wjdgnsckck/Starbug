package starbug.model.entity.product;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import starbug.model.dto.product.ProductDto;
import starbug.model.entity.order.OrderDetailsEntity;
import starbug.model.entity.resoures.RecipeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@DynamicInsert
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class ProductEntity { // 상품 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pno; // 상품 식별번호

    @Column(name = "pname", length = 50, nullable = false, unique = true)
    private String pname; // 상품 이름

    @Column(name = "pprice", nullable = false)
    private int pprice; // 상품 가격

    @ColumnDefault("0")
    @Column(name = "pevent")
    private int pevent; // 이벤트 여부

    @ToString.Exclude // 상품 카테고리 FK
    @JoinColumn(name = "pcno")
    @ManyToOne
    private ProductCategoryEntity productCategoryEntity;

    @Builder.Default // 레시피 내역
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<RecipeEntity> recipeEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetailsEntities = new ArrayList<>();

    public ProductDto toProductDto() {
        return ProductDto.builder()
                .pno(this.pno)
                .pname(this.pname)
                .pprice(this.pprice)
                .pcname(this.productCategoryEntity.getPcname())
                .pcno(this.productCategoryEntity.getPcno())
                .pevent(this.pevent)
                .build();
    }
}
