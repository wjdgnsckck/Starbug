package starbug.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.dto.staff.SalaryDto;
import starbug.service.order.PurchaseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/PurchaseForSales")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    // 1. 총 발주 내역 출력하기 ===============================================
    @GetMapping("/doAllPurchase")
    public PageDto getPurchaseList(@RequestParam int page,
                                         @RequestParam String key,
                                         @RequestParam String keyword){
        return purchaseService.getPurchaseList(page,key,keyword);
    }

    // 2. 총 급여 내역 출력하기 ===============================================
    @GetMapping("/doAllSalary")
    public PageDto getSalaryList(@RequestParam int page,
                                         @RequestParam String key,
                                         @RequestParam String keyword){
        System.out.println(" 급여 출력 메소드 입장 하나?? ");
        return purchaseService.getSalaryList(page,key,keyword);
    }

}
