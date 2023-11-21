package starbug.model.dto.order;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class IncomeDto {

    private int  매출수익;
    private int  매출외수익;
    private int  제품원가;
    private int  매출할인;
    private int  직원급여;
    private int  영업외비용;
    private int  부가세차감전순이익;
    private int  부가세액;
    private int  기간내순이익;

}
