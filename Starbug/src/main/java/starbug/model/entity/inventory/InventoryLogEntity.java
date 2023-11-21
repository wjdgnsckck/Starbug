package starbug.model.entity.inventory;

import lombok.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.resoures.ResouresEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
@ToString
@Builder
@Table(name = "inventoryLog")
public class InventoryLogEntity{ // 재고관리 로그 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inlogno;              // 재고로그 PK
    @Column(name ="inlogdate",nullable = false )
    private LocalDateTime inlogdate;  //로그날짜
    @Column(name ="inloghistory",nullable = false )
    private double inloghistory ;        //입출 내역


    @ToString.Exclude // 재료 번호 FK
    @JoinColumn(name = "resno")
    @ManyToOne
    private ResouresEntity resouresEntity;

    public ResouresDto toInventoryLogDto() {

        return ResouresDto.builder()
                .inlogno(this.inlogno)
                .inlogdate(this.inlogdate)
                .inloghistory(this.inloghistory)
                .resno((resouresEntity.getResno()))
                .build();
    }

}
