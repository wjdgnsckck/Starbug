package starbug.model.entity.staff;

import lombok.*;
import starbug.model.dto.staff.SalaryDto;
import starbug.model.entity.BaseTime;

import javax.persistence.*;

@Entity
@Table(name = "salary")
@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString@Builder
public class SalaryEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slno;
    @Column(nullable = false)
    private int sbasepay;
    @Column(nullable = false)
    private int sincentive;
    @Column(nullable = false)
    private int sdeductible;
    @Column(nullable = false)
    private String sdate;


    @ToString.Exclude
    @JoinColumn(name="sno")
    @ManyToOne
    private StaffListEntity staffListEntity;

    // dto 변환
    public SalaryDto toDto(){
        return SalaryDto.builder()
                .slno(this.slno)
                .sbasepay(this.sbasepay)
                .sincentive(this.sincentive)
                .sdeductible(this.sdeductible)
                .sdate(this.sdate)
                .cdate(this.getCdate())
                .udate(this.getUdate())
                .sno(this.staffListEntity.getSno())
                .sname(this.staffListEntity.getSname())
                .build();

    }//toDto
}//class
