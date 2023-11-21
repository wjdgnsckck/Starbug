package starbug.model.dto.page;

import lombok.*;
import starbug.model.dto.member.CouponHistoryDto;
import starbug.model.dto.member.StarBugMemberDto;
import starbug.model.dto.order.OrdersDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.dto.staff.SalaryDto;
import starbug.model.dto.staff.StaffListDto;
import starbug.model.dto.staff.TimeAndAttendanceDto;

import java.util.List;

@Builder@Getter
@Setter@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    //여기에 원하는 게시물 필드 넣어주세요! -정훈-

    //-------------예지--------------------//
    List<OrdersDto> ordersDtoList;
    List<ResouresDto> parcellogDtos;
    List<SalaryDto> salaryDtos;


    //-------------병철---------------------//
    List<StaffListDto> staffListDtos;
    List<TimeAndAttendanceDto> timeAndAttendanceDtos;


    //-------------정훈---------------------//
    //1. 반환된 게시물 (재료 페이징 처리)
    List<ResouresDto> ResouresCount;
    List<ResouresDto> inventoryCount ; //2. 재고 로그 페이징 처리

    //-------------규리---------------------//
    List<StarBugMemberDto> membersDtos; // 회원페이징처리
    List<CouponHistoryDto> couponDtos; // 쿠폰 페이징처리

    //2. 반환된 총 페이지수
    private int totalPage;
    //3. 반환된 총 게시물수
    private long totalCount;
}
