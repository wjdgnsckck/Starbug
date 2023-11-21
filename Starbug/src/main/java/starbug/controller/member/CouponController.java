package starbug.controller.member;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starbug.model.dto.member.CouponHistoryDto;
import starbug.model.dto.page.PageDto;
import starbug.service.member.CouponService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    // 쿠폰 카테고리 등록
    @PostMapping("/postcc")
    public boolean couponWrite(@RequestBody CouponHistoryDto couponHistoryDto){
        //System.out.println("컨트롤러 쿠폰 등록 : "+couponHistoryDto);
        boolean result = couponService.couponWrite(couponHistoryDto);
        return result;
    }

    // 쿠폰 카테고리 출력
    @GetMapping("/getcc")
    public PageDto couponGet(@RequestParam int page , @RequestParam int view  ){
        PageDto result = couponService.couponGet(page , view);

        return result;
    }

    // 쿠폰 카테고리 삭제 (회원이 쿠폰 다 썼을때만 삭제 가능)
    @DeleteMapping("/deletecc")
    public boolean couponDelete( @RequestParam int ccno){
        boolean result = couponService.couponDelete(ccno);
        //System.out.println("컨트롤러 쿠폰 삭제 "+ result);
        //System.out.println("컨트롤러 쿠폰 삭제 "+ ccno);
        return result;
    }

    // 쿠폰 회원에게 발급 (일괄)
    @PostMapping("/membercouponw")
    public boolean memberCouponw(@RequestBody Map<String, Object> requestData ){
        int ccno = Integer.parseInt((String) requestData.get("ccno"));  // 받아온 객체를 형변환
        List<String> mnoList = (List<String>) requestData.get("mno");

        System.out.println("컨트롤러 ccno :" + ccno);
        for (int i=0; i<mnoList.size(); i++){
            System.out.println("컨트롤러 mnoList :" + mnoList.get(i));
        }

        boolean result = couponService.memberCouponw(ccno , mnoList );
        return result;
    }


}
