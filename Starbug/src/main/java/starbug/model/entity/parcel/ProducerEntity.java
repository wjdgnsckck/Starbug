package starbug.model.entity.parcel;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.resoures.ResouresEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//거래처 엔티티
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
@Entity
@Table( name = "producer" )
public class ProducerEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int prno; // 거래처 번호

    @Column( name = "prname" , length = 20 , nullable = false)
    private String prname; // 거래처이름

    @Builder.Default // 재료들
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producerEntity", cascade = CascadeType.ALL)
    private List<ResouresEntity> resouresEntities = new ArrayList<>();

    public ResouresDto toProducerDto() {

        return ResouresDto.builder()
                .prno(this.prno)
                .prname(this.prname)
                .build();
    }

}