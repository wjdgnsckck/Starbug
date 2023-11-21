package starbug.model.dto.staff;

import lombok.*;
import starbug.model.entity.staff.SalaryEntity;

import java.time.LocalDateTime;

@AllArgsConstructor@NoArgsConstructor@Setter@Getter
@ToString@Builder
public class SalaryDto {

    private int slno;
    private int sbasepay;           // 기본급
    private int sincentive;         // 장려금
    private int sdeductible;        // 차감
    private String sdate;    // 지급일

    private LocalDateTime cdate;    // 등록일
    private LocalDateTime udate;    // 수정일

    private int sno;
    private String sname;

    // entity 로 저장위해 변환
    public SalaryEntity saveEntity(){
        return SalaryEntity.builder()
                .sbasepay(this.sbasepay)
                .sincentive(this.sincentive)
                .sdeductible(this.sdeductible)
                .sdate(this.sdate)

                .build();
    }// saveEntity
} // class
