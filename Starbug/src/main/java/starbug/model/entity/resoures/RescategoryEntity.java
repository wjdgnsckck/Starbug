package starbug.model.entity.resoures;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Setter@Getter
@NoArgsConstructor@AllArgsConstructor
@ToString
@Table(name = "rescategory")
public class RescategoryEntity  { // 재료 카테고리 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rescno;         //카테고리 번호
    @Column(name = "rescname",nullable = false , unique=true)
    private String rescname;    //카테고리 이름

    @Builder.Default // 재료들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rescategoryEntity", cascade = CascadeType.ALL)
    private List<ResouresEntity> resouresEntities = new ArrayList<>();

    public ResouresDto toRescategoryDto() {

        return ResouresDto.builder()
                .rescno(this.rescno)
                .rescname(this.rescname)
                .build();
    }

}
