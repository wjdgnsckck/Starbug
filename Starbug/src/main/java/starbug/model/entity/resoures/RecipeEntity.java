package starbug.model.entity.resoures;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.product.ProductEntity;

import javax.persistence.*;

@Entity
@Table(name = "recipe")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class RecipeEntity { // 레시피 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int rno; // 레시피 식별번호
    double rquantity; // 재료 필요 수량

    @ToString.Exclude
    @JoinColumn(name = "pno")
    @ManyToOne
    private ProductEntity productEntity;

    @ToString.Exclude
    @JoinColumn(name = "resno")
    @ManyToOne
    private ResouresEntity resouresEntity;

  public ResouresDto toRecipeDto() {
        return ResouresDto.builder()
                .rno(this.rno)
                .rquantity(this.rquantity)
                .resno(this.resouresEntity.getResno())
                .pno(this.productEntity.getPno())
                .build();
    }
}
