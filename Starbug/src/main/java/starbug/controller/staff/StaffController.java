package starbug.controller.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.staff.SalaryDto;
import starbug.model.dto.staff.StaffListDto;
import starbug.model.dto.staff.TimeAndAttendanceDto;
import starbug.service.staff.SalaryService;
import starbug.service.staff.StaffService;
import starbug.service.staff.TimeAndAttendanceService;

@RestController
@RequestMapping("/staff")

public class StaffController {

    @Autowired
    private StaffService staffService;

    //-------------------인사---------------------//
    // 인사 등록
    @PostMapping("/staffPost")
    public boolean staffPost(StaffListDto staffListDto) {
        boolean result = staffService.staffPost(staffListDto);

        return result;
    }

    // 인사 정보 호출
    @GetMapping("/staffGet")
    public PageDto staffGet(@RequestParam int page , @RequestParam String keyword){

        return staffService.staffGet(page , keyword);
    }

    // 인사 정보 수정
    @PutMapping("/staffPut")
    public boolean staffPut(StaffListDto staffListDto){
        boolean result = staffService.staffPut(staffListDto);
        return result;
    }

    // 인사 정보 삭제
    @DeleteMapping("/staffDelete")
    public boolean staffDelete(@RequestParam int sno){
        boolean result = staffService.staffDelete(sno);
        return result;
    }

    // 인사 정보 개별 출력
    @GetMapping("/staffInfoGet")
    public StaffListDto staffInfoGet(@RequestParam int sno){

        return staffService.staffInfoGet(sno);
    }

    // 인사정보 이름으로 검색
    @GetMapping("/staffNameGet")
    public StaffListDto staffNameGet(@RequestParam String sname){

        return staffService.staffNameGet(sname);
    }

    // 인사정보 이미지 수정
    @PutMapping("staffImgUpdate")
    public boolean staffImgUpdate(StaffListDto staffListDto){


        return staffService.staffImgUpdate(staffListDto);
    }

    //-------------------인사 end---------------------//


    //-------------------급여---------------------//

    @Autowired
    private SalaryService salaryService;
    // 급여 등록
    @PostMapping("/salaryPost")
    public boolean salaryPost(SalaryDto salaryDto){
        boolean result= salaryService.salaryPost(salaryDto);
        return result;
    }

    // 급여 정보 호출
    @GetMapping("/salaryGet")
    public PageDto salaryGet(@RequestParam int page , @RequestParam int sno){

        return salaryService.salaryGet(page , sno);
    }

    // 급여 정보 수정
    @PutMapping("/salaryPut")
    public boolean salaryPut(SalaryDto salaryDto){
        System.out.println("★★★★★★ 서비스 급여정보 수정 : " +salaryDto);
        boolean result = salaryService.salaryPut(salaryDto);
        return result;
    }

    // 급여 정보 삭제
    @DeleteMapping("/salaryDelete")
    public boolean salaryDelete(@RequestParam int slno){
        boolean result = salaryService.salaryDelete(slno);
        return result;
    }

    // 급여 정보 개별 호출
    @GetMapping("/salaryGetInfo")
    public SalaryDto salaryGetInfo(@RequestParam int slno){
        System.out.println("★★★★★★급여정보 개별호출 slno" + slno );
        return salaryService.salaryGetInfo(slno);
    }
    //-------------------급여 end---------------------//


    //-------------------근태---------------------//

    @Autowired
    private TimeAndAttendanceService timeAndAttendanceService;

    // 근태 등록
    @PostMapping("/taaPost")
    public boolean taaPost( @RequestBody TimeAndAttendanceDto timeAndAttendanceDto){
        boolean result = timeAndAttendanceService.taaPost(timeAndAttendanceDto);

        return result;
    }
    // 근태 정보 호출
    @GetMapping("/taaGet")
    public PageDto taaGet(@RequestParam int page , @RequestParam int sno){

        return timeAndAttendanceService.taaGet(page , sno);
    }

    // 근태 정보 수정
    @PutMapping("/taaPut")
    public boolean taaPut(TimeAndAttendanceDto timeAndAttendanceDto){
        boolean result = timeAndAttendanceService.taaPut(timeAndAttendanceDto);
        return result;
    }

    // 근태 삭제
    @DeleteMapping("/taaDelete")
    public boolean taaDelete(@RequestParam int tno){
        boolean result= timeAndAttendanceService.taaDelete(tno);
        return result;
    }

    // 근태 정보 개별 호출
    @GetMapping("/taaGetInfo")
    public TimeAndAttendanceDto taaGetInfo (@RequestParam int tno){

        return timeAndAttendanceService.taaGetInfo(tno);
    }

    //-------------------근태 end---------------------//




}
