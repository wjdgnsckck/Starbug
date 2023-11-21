package starbug.service.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starbug.model.dto.order.IncomeDto;
import starbug.model.repository.order.OrderDetailsEntityRepository;
import starbug.model.repository.order.OrdersEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;
import starbug.model.repository.staff.SalaryEntityRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class IncomeService {

    @Autowired
    private OrdersEntityRepository ordersRepository;
    @Autowired
    private OrderDetailsEntityRepository orderDetailsRepository;
    @Autowired
    private ResouresEntityRepository resouresEntityRepository;
    @Autowired
    private SalaryEntityRepository salaryEntityRepository;

    // 순이익
    public IncomeDto getIncome(String startDate, String endDate){
        System.out.println("IncomeService.getIncome");

        // 1. 매출 수익과 매출 할인 구하기
        Map<String, Integer> objects = ordersRepository.findByIncomeSales(startDate, endDate);
        int total_totalprice = Integer.parseInt(String.valueOf(objects.get("total_totalprice")));
        int total_paid = Integer.parseInt(String.valueOf(objects.get("total_paid")));
        int discount = total_totalprice - total_paid;

        System.out.println("매출수익"+total_totalprice);
        System.out.println("실결제금액"+total_paid);
        System.out.println("매출할인"+discount);

        // 2. 제품원가 구하기
        int cost  = resouresEntityRepository.findByIncomePurchase(startDate, endDate);
        System.out.println("제품원가"+cost );

        // 3. 직원급여 구하기 [sbasepay + sincentive - sdeductible]
        int salary = salaryEntityRepository.findByIncomeSalary(startDate, endDate);
        System.out.println("직원급여"+salary);


        // 4. 모든 계정과목 계산해서 하나의 DTO로 만들어 반환하기
            // 매출수익 = 총 주문 금액
            // 매출외수익 = 주문외 수익이 들어가지만 ★현프로젝트에서는 없다고 가정
            // 제품원가 = 재고 사용량 or 총 발주액
            // 매출할인 = 주문금액 - 실결제금액
            // 직원급여 = 급여지금액
            // 영업외비용 = 임대료 등등이 들어가지만 ★현프로젝트에는 없다고 가정
            // 부가세차감전순이익 = 매출수익 - 제품원가 - 매출할인 - 직원급여 - 영업외비용
            // 부가세액 = 실결제금액*10%
            // 기간내순이익 = 부가세차감전순이익 - 부가세액
        return  IncomeDto.builder()
                .매출수익( total_totalprice )
                .매출외수익( 0 )
                .제품원가( cost )
                .매출할인( discount )
                .직원급여( salary )
                .영업외비용( 0 )
                .부가세차감전순이익( total_totalprice-cost-discount-salary )
                .부가세액( (total_paid/10) )
                .기간내순이익( (total_totalprice-cost-discount-salary)-(total_paid/10) )
                .build();
    }



}
