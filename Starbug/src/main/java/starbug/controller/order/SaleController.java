package starbug.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starbug.model.dto.order.ChartDto;
import starbug.model.dto.order.OrdersDto;
import starbug.model.dto.page.PageDto;
import starbug.service.order.SaleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/saleController")
public class SaleController {

    @Autowired
    private SaleService saleService;

    // 1. 총 주문 출력하기
    @GetMapping("/doAll")
    public PageDto getOrderList(@RequestParam int page,
                                @RequestParam String key,
                                @RequestParam String keyword){
        System.out.println("SaleController.getOrderList");
        return saleService.getOrderList(page, key, keyword);
    }

    // 2. 선택된 주문의 상세정보 출력하기
    @GetMapping("/doOne")
    public List<OrdersDto> getOrderOne( @RequestParam int ono ){
        System.out.println("SaleController.getOrderOne");
        return saleService.getOrderOne(ono);
    }

    // 3. 날짜 설정 후 매입(발주) 총액 구하기
    @GetMapping("/getPaid")
    public int getPaidForResoures(@RequestParam String cdate){
        System.out.println("SaleController.getPaidForResoures");
        return saleService.getPaidForResoures( cdate );
    }

    // 4. 매입 매출 자료 차트 출력
    @GetMapping("/getChartForSales")
    public ChartDto getChartForSales (@RequestParam String salesDate ){

        return saleService.getChartForSales( salesDate );
    }

    // 5. 상품 차트 출력
    @GetMapping("/getChartForProduct")
    public List<Map< String, Object >> getChartForProduct (@RequestParam String startDate,
                                                           @RequestParam String endDate ){

        return saleService.getChartForProduct( startDate, endDate );
    }
}
