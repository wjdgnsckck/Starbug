package starbug.model.dto.staff;

import lombok.*;
import starbug.model.entity.staff.TimeAndAttendanceEntity;

import java.time.LocalDateTime;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@ToString@Builder
public class TimeAndAttendanceDto {

    private int tno;
    private String tstate; // 근태 상태 지각 , 결근
    private String tdate;    // 발생 일자

    private LocalDateTime cdate;    // 등록 일자
    private LocalDateTime udate;    // 수정 일자

    private int sno;


    // entity 로 저장위해 변환
    public TimeAndAttendanceEntity saveEntity(){
        return TimeAndAttendanceEntity.builder()
                .tstate(this.tstate)
                .tdate(this.tdate)
                .build();
    }//saveEntity
}//class