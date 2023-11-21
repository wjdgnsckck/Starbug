package starbug.controller.parcel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import starbug.model.dto.resoures.ResouresDto;
import starbug.service.parcel.ParcelService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/parcel")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    // 날짜별로 발주 내역 확인 get
    @GetMapping("/getpa")
    public List<ResouresDto> getParcels( @RequestParam String cdate ){

        return parcelService.getParcels( cdate );
    }

    // 발주 할 떄 발주 등록
    @PostMapping("/postpa")
    public boolean postParcel( @RequestBody ResouresDto resouresDto ){

        System.out.println("resouresDto" + resouresDto);

        boolean result = parcelService.postParcel( resouresDto );

       /*if( result ) {
            LocalDate localDate = LocalDate.now();
            parcelService.putParcel(localDate);
        }*/

        return result;
    }

    // 발주 도착용 날짜 수정함수
    @PutMapping("/putpa")
    public boolean putParcel( @RequestBody ResouresDto resouresDto ){

        boolean result = parcelService.putParcel(resouresDto.getPano() );
        return result;
    }

    // 환불용 삭제 함수
    @DeleteMapping("/deletepa")
    public boolean cancelParcel( @RequestParam int pano ){

        boolean result = parcelService.deleteParcel(pano);
        return result;
    }

    // 발주 위해 재료 목록 불러오기
    @GetMapping("/getresour")
    public List<ResouresDto> getResoures( @RequestParam int rescno ){

        return parcelService.getResoures( rescno );
    }

    // 재료 목록 구분하기 위해 카테고리 불러오기
    @GetMapping("/getrescate")
    public List<ResouresDto> getResouresCategory(){

        return parcelService.getResouresCategory( );
    }


    // 날짜 중복제거해서 가져오는 함수
    @GetMapping("/getdate")
    public List<ResouresDto> getDateList(){

        return parcelService.getDateList();
    }

    // 환불 신청 -> pastate 를 1(true) 로 바꿔준다 -> 스케쥴러로 삭제 예정
    @PutMapping("/refundpa")
    public boolean refundParcel( @RequestBody ResouresDto resouresDto){

        int pano = resouresDto.getPano();

        return parcelService.refundParcel( pano );
    }

    // 오늘 발주 목록이 있다면 재고에 뿌려주기 , 없으면 가장 최근 날짜의 재고 뿌려줌
    @GetMapping("/todaygetpa")
    public List<ResouresDto> todayGetParcel(){

        return parcelService.todayGetParcel();
    }

}
