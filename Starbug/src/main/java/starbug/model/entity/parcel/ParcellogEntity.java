package starbug.model.entity.parcel;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.BaseTime;
import starbug.model.entity.resoures.ResouresEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

// 발주 로그 엔티티
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter @ToString
@Builder
@Entity
@Table(name = "parcel_log")
public class ParcellogEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pano; // 발주 식별 번호

    @Column( name = "pcount" , nullable = false)
    private int pacount; // 주문 개수

    @Column( name = "pastate" , nullable = false)
    @ColumnDefault("false")
    private boolean pastate; // 발주 상태

    @ToString.Exclude // 재료번호 FK
    @JoinColumn(name = "resno")
    @ManyToOne
    private ResouresEntity resouresEntity;

    public ResouresDto toParcellogDto () {

        return ResouresDto.builder()
                .pano(this.pano)
                .pacount(this.pacount)
                .pastate(this.pastate)
                .resno(this.resouresEntity.getResno())
                .cdate(this.getCdate())
                .udate(this.getUdate())
                .resname(this.resouresEntity.getResname())
                .resprice(this.resouresEntity.getResprice())
                .build();
    }

    public ResouresDto toDateListDto (){

        return ResouresDto.builder()
                .cdate(this.getCdate())
                .build();

    }

}