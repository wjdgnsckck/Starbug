package starbug.model.entity.staff;

import lombok.*;
import starbug.model.dto.staff.TimeAndAttendanceDto;
import starbug.model.entity.BaseTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "timeAndAttendance")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString @Builder
public class TimeAndAttendanceEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tno;                // pk
    @Column(nullable = false)
    private String tstate;
    @Column(nullable = false)
    private String tdate;             // 등록일자

    @ToString.Exclude
    @JoinColumn(name ="sno_fk")
    @ManyToOne
    private StaffListEntity staffListEntity;

    public TimeAndAttendanceDto toDto(){

        return TimeAndAttendanceDto.builder()
                .tno(this.tno)
                .tstate(this.tstate)
                .tdate(this.tdate)
                .cdate(this.getCdate())
                .cdate(this.getUdate())
                .sno(this.staffListEntity.getSno())
                .build();
    }

}//class
