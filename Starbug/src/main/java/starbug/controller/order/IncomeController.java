package starbug.controller.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starbug.model.dto.order.IncomeDto;
import starbug.service.order.IncomeService;

@RestController
@RequestMapping("/incomeController")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // 순이익
    @GetMapping("/getIncome")
    public IncomeDto getIncome(@RequestParam String startDate,
                               @RequestParam String endDate){

        System.out.println("IncomeController.getIncome");
        return incomeService.getIncome(startDate, endDate);
    }
}
