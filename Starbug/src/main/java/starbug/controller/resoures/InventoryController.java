package starbug.controller.resoures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import starbug.StarBugStart;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.service.resoures.InventoryService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;




@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    //[C] : 제품 판매시 재고의 값 수정과 재고 로그에 insert하는 메소드
    @GetMapping("/count")
    public boolean insertCount(int pno){
        System.out.println("pno = " + pno);
        System.out.println("InventoryController.insertCount");
        return inventoryService.insertCount(pno);

    }
    //[R] : 제품 검색전 이름에 맞는 재료가 있는지 확인하는 메소드

    @GetMapping("/exist")
    public boolean exist(@RequestParam String resname){
        return inventoryService.exist(resname);
    }

    //[R] : resname 에 맞는 검색 기능 구현하기
    @GetMapping("/serach")
    public ResouresDto onSerach(@RequestParam String resname){
        return inventoryService.onSerach(resname);
    }

    //[R] : 처음에 재료(재고)를 전체 출력해주는 메소드(페이징처리 해야함.)
    @GetMapping("/resoures")
    public PageDto getResoures(@RequestParam int page,@RequestParam int view
            ,@RequestParam String keyword){
        return  inventoryService.getResoures(page,view,keyword);
    }

    //[R] : 재고 로그 출력 해주는 메소드(날짜를 입력받아야함, 발주관리 재고 로그 출력)
    @GetMapping("/recouresLog")
    public List<ResouresDto> getInventoryLog(@RequestParam String date){
        System.out.println("InventoryController.getInventoryLog");
        return inventoryService.getInventoryLog(date);
        //
    }
    //[R] : 재고 로그에 값이 존재하는지 여부
    @GetMapping("/existlog")
    public boolean existDate(@RequestParam String date){
        System.out.println("date = " + date);
        return inventoryService.existDate(date);
    }



    //[R] : 재고 로그 출력 해주는 메소드 (페이징처리,재고 로그 페이지 출력)
    @GetMapping("/printLog")
    public PageDto getInventory(@RequestParam int page,@RequestParam String keyword){
        System.out.println("InventoryController.getInventory");
        return inventoryService.getInventory(page,keyword);
    }



    //[C] : 입력받은값을 저장해주는 메소드
    @PostMapping("/loginsert")
    public int insertResoures(ResouresDto dto){
        System.out.println("InventoryController.insertResoures");
        System.out.println("dto = " + dto);
        if(inventoryService.insertResoures(dto)){return 1;}
        return 2;

    }
    //[U] : 재료를 수정해주는 메소드
    @PutMapping("/resouresupdate")
    public boolean updateResoures(ResouresDto dto){
        System.out.println("InventoryController.updateResoures");
        System.out.println("ResouresDto = " + dto);

        return inventoryService.updateResoures(dto);
    }
    //[D] : 재료를 삭제해주는 메소드
    @DeleteMapping("/logdelete")
    public boolean deleteLog(@RequestParam int inlogno){
        System.out.println("InventoryController.deleteResoures");
        System.out.println("inlogno = " + inlogno);
        return inventoryService.deleteLog(inlogno);
    }


    // [R] : 모든 재료 가져와주는 메소드
    @GetMapping("/allresoures")
    public List<ResouresDto> getAllResoures() {
        return inventoryService.getAllResoures();
    }
}
