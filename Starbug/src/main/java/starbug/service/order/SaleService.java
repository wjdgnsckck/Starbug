package starbug.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import starbug.model.dto.order.ChartDto;
import starbug.model.dto.order.OrdersDto;
import starbug.model.dto.page.PageDto;
import starbug.model.entity.order.OrderDetailsEntity;
import starbug.model.entity.order.OrdersEntity;
import starbug.model.repository.order.OrderDetailsEntityRepository;
import starbug.model.repository.order.OrdersEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private OrdersEntityRepository ordersRepository;
    @Autowired
    private OrderDetailsEntityRepository orderDetailsRepository;
    @Autowired
    private ResouresEntityRepository resouresEntityRepository;

    // 1. 총 주문 출력하기 ============================================================================================
    @Transactional
    public PageDto getOrderList( int page, String key, String keyword){
        System.out.println("SaleService.getOrderList");

        if(keyword.equals("비회원") || keyword.equals("환불") ){ keyword = "1"; }
        else if(keyword.equals("결제") || keyword.equals("결제완료")){ keyword = "0"; }

        // 페이징 처리
        Pageable pageable  = PageRequest.of(page-1, 10
                , Sort.by( Sort.Direction.DESC, "odate"));

        // 주문 모두 꺼내오기
        Page<OrdersEntity> ordersEntityList = ordersRepository.findBySearch( key, keyword, pageable );
        System.out.println("ordersEntityList :" +ordersEntityList);



        // 타입 변환해서 담아둘 곳
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        // 타입변환하고 담기
        ordersEntityList.forEach( list -> {
            ordersDtoList.add( list.toOrdersDto() );
        });


        // 총 페이지수
        int totalPages = ordersEntityList.getTotalPages();
        // 총 게시물수
        long totalCount = ordersEntityList.getTotalElements();

        // DTO 구성해서 반환하기
        PageDto resuldto = PageDto.builder()
                .ordersDtoList( ordersDtoList )
                .totalPage( totalPages )
                .totalCount( totalCount )
                .build();

        System.out.println( "ordersDtoList : "+ordersDtoList);
        return resuldto;
    }

    // 1-2. 엑셀 다운로드를 위한 주문 내역 호출
    @Transactional
    public List<OrdersDto> getOrderListExcelDownload( ) {

        List<OrdersEntity> ordersEntityList = ordersRepository.findAll();
        List<OrdersDto> ordersDtoList = new ArrayList<>();

        ordersEntityList.forEach( order -> {
            ordersDtoList.add( order.toOrdersDto() );
        });

        return ordersDtoList;
    }





    // 2. 선택된 주문의 상세정보 출력하기 ============================================================================================
    @Transactional
    public List<OrdersDto> getOrderOne(int ono){
        System.out.println("SaleService.getOrderOne");

        List<Map< Integer, Object >> orderDetailsList = orderDetailsRepository.findByOno(ono);
        System.out.println(orderDetailsList);

        List<OrdersDto> resultDtos = orderDetailsList.stream().map( list -> {
            Object odateObj = list.get("odate");

            LocalDateTime localdateTime = null;
            if (odateObj instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) odateObj;
                localdateTime = timestamp.toLocalDateTime();
            }
            return OrdersDto.builder()
                    .ono((Integer) list.get("ono"))
                    .ostate((int) list.get("ostate"))
                    .paid((int) list.get("paid"))
                    .totalprice((int) list.get("totalprice"))
                    .mno((int) list.get("mno"))
                    .odtno((int) list.get("odtno"))
                    .pno((int) list.get("pno"))
                    .pname((String) list.get("pname"))
                    .mname((String) list.get("mname"))
                    .odate( localdateTime )
                    .build();
        }).collect(Collectors.toList());

        System.out.println("resultDtos => "+resultDtos);
        return resultDtos;
    }

    // 3. 매입(발주) 총액 구하기 ============================================================================================
    @Transactional
    public int getPaidForResoures( String cdate ){
        System.out.println("SaleService.getPaidForResoures");
        System.out.println("cdate = " + cdate);
        int result = resouresEntityRepository.findByPacelLogForPaid(cdate);
        return result;
    }

    // 4. 매입 매출 자료 차트 출력 ==========================================================
    @Transactional
    public ChartDto getChartForSales ( String year ){
        System.out.println("매입매출차트서비스");
        System.out.println("year = " + year);

        // 매출액 DB에서 탐색하기
        List<Map< String, Object >> Sdata
                = ordersRepository.findBySearchChartForSales(year);
        System.out.println("Sdata => "+Sdata);

        // 매입액 DB에서 탐색하기
        List<Map< String, Object >> Bdata
                = ordersRepository.findBySearchChartForBuy(year);
        System.out.println("Bdata => "+Bdata);

        // 두 리스트를 하나로 합치기
        ChartDto resultData = ChartDto.builder().Bdata(Bdata).Sdata(Sdata).build();

        return resultData;
    }


    // 5. 상품 차트 출력 ===========================================================================
    @Transactional
    public List<Map< String, Object >> getChartForProduct ( String startDate, String endDate ){
        System.out.println("상품차트서비스");
        System.out.println("startDate = " + startDate);
        System.out.println("endDate = " + endDate);

        // DB에서 탐색하기
        List<Map< String, Object >> Pdata
            = ordersRepository.findBySearchChartForProduct(startDate, endDate);
        System.out.println("Pdata => "+Pdata);
        return Pdata;

    }
}


//        // Dto로 변환 ( Dto 쓸 필요 없다고 한다...)
//        List<ChartDto> resultList = Pdata.stream().map( list -> {
//            return ChartDto.builder()
//                            .searchDate((String) list.get("month"))
//                            .pcno((Integer) list.get("pcno"))
//                            .pname((String) list.get("pname"))
//                            .percentage((Integer) list.get("percentage"))
//                            .build() ;
//        }).collect(Collectors.toList());
//        System.out.println( "차트 상품 resultList : "+resultList);