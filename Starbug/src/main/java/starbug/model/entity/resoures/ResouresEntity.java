package starbug.model.entity.resoures;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.inventory.InventoryLogEntity;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.entity.parcel.ProducerEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//재료 엔티티
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString @Builder
@Entity
@Table( name = "resources" )
public class ResouresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resno; // 재료번호

    @Column( name = "resname" , length = 20 , nullable = false)
    private String resname; // 재료 이름

    @Column( name = "resprice" , nullable = false)
    private int resprice; // 재료 가격

    @ToString.Exclude // 재료 카테고리 FK
    @JoinColumn(name ="rescno")
    @ManyToOne
    private RescategoryEntity rescategoryEntity;

    @ToString.Exclude // 거래처 FK
    @JoinColumn(name = "prno")
    @ManyToOne
    private ProducerEntity producerEntity;

    @Builder.Default // 레시피들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resouresEntity",cascade = CascadeType.ALL)
    private List<RecipeEntity> recipeEntities = new ArrayList<>();

    @Builder.Default // 발주로그들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resouresEntity",cascade = CascadeType.ALL)
    private List<ParcellogEntity> parcellogEntities = new ArrayList<>();

    @Builder.Default // 재고로그들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resouresEntity",cascade =CascadeType.DETACH) //삭제시 재고 로그는 남아야함! 그래서 DETACH사용
    private List<InventoryLogEntity> inventoryLogEntities = new ArrayList<>();



    public ResouresDto toResouresDto() {

        return ResouresDto.builder()
                .resno(this.resno)
                .resname(this.resname)
                .resprice(this.resprice)
                .rescno(this.rescategoryEntity.getRescno())
                .prno(this.producerEntity.getPrno())
                .build();
    }

    public  ResouresDto toAllResouresDtos() {

        return ResouresDto.builder()
                .resno(this.resno)
                .resname(this.resname)
                .resprice(this.resprice)
                .rescno(this.rescategoryEntity.getRescno())
                .rescname(this.rescategoryEntity.getRescname())
                .prno(this.producerEntity.getPrno())
                .prname(this.producerEntity.getPrname())
                .build();

    }

}