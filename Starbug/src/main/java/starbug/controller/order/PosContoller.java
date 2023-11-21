package starbug.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starbug.service.order.PosService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pos")
public class PosContoller {

    @Autowired
    private PosService posService;

    // 1. 주문하기 ===========================================================================================
    @PostMapping("/do")
    public boolean postOrders ( @RequestBody Map<String, Object> objectMap ) {
        System.out.println("PosContoller.postOrders");
        System.out.println("objectMap = " + objectMap);
        return posService.postOrders(objectMap);
    }

    // 2. 쿠폰 정보 호출하기 ===========================================================================================
    @GetMapping("/getCoupon")
    public List<Map<String , Object>> getMemberCoupon( @RequestParam int mno ) {
        System.out.println("PosContoller.getMemberCoupon");
        return posService.getMemberCoupon( mno );
    }

    // 3. 환불하기 ===========================================================================================
    @PutMapping("/cancelOrder")
    public boolean cancelOrder( @RequestBody Map<String, Integer> requestBody ) {
        System.out.println("PosContoller.cancelOrder");
        int ono = requestBody.get("ono");
        System.out.println("ono = " + ono);

        return posService.cancelOrder( ono );
    }




}
