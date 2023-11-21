package starbug.model.dto.order;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChartDto {

//    private String searchDate;
//    private String pname;
//    private int pcno;
//    private int percentage;
    private List<Map< String, Object >> Sdata;
    private List<Map< String, Object >> Bdata;


}
