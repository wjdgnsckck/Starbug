package starbug.controller.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import starbug.model.dto.order.OrdersDto;
import starbug.model.dto.product.ProductDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.service.order.SaleService;
import starbug.service.parcel.ParcelService;
import starbug.service.product.ProductService;
import starbug.service.resoures.InventoryService;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/excel") // 엑셀 다운로드 컨트롤러
public class ExcelController {

    @Autowired
    private ParcelService parcelService;  //발주
    @Autowired
    private InventoryService inventoryService; // 재료,재고로그
    @Autowired
    private ProductService productService; // 상품

    @Autowired
    private SaleService saleService; // 매출

    @GetMapping("/parcellog")
    public void parcelEexelDownload ( @RequestParam String cdate , HttpServletResponse response ) {

        try {

            // db 에서 꺼내올 데이터 저장할 리스트
            // view 에서 가져온 조회일(cdate) 로 검색해서 해당날짜 발주내역 가져온다
            List<ResouresDto> resoures = parcelService.getParcels(cdate);
            System.out.println("resoures = " + resoures);

            // poi 라이브러리의 Workbook
            // ms오피스의 확장자를 사용 할 수 있게 해줌
            Workbook workbook = new XSSFWorkbook(); // .xlsx 확장자용
            Sheet sheet = workbook.createSheet("parcelLog"); // sheet 이름 (한글은 오류날 가능성 up)

            Row row; // 행
            Cell cell; // 열
            int rowno = 0;

            // 숫자가 증가해야 옆 , 아래로 내려감
            row = sheet.createRow(rowno++);

            // index 번쨰 행 선언
            cell = row.createCell(0);
            // 선언한 행에 value 넣기
            cell.setCellValue("상품명");

            // 반복
            cell = row.createCell(1);
            cell.setCellValue("수량");

            cell = row.createCell(2);
            cell.setCellValue("개별가격");

            cell = row.createCell(3);
            cell.setCellValue("총가격");

            cell = row.createCell(4);
            cell.setCellValue("주문일");

            cell = row.createCell(5);
            cell.setCellValue("도착일");

            // 셀 스타일 지정하기 위한 선언
            CellStyle cellStyle = workbook.createCellStyle();

            // 셀 백그라운드 컬러 - 연한회색( brown 으로 시도해봤지만 촌스러웠음 )
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                                    // 채우기
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 폰트 스타일
            Font font = workbook.createFont();
            // bold
            font.setBold(true);
            cellStyle.setFont(font);

            // 각각의 행에 스타일 적용하기
            for (int i = 0; i < row.getLastCellNum(); i++){
                row.getCell(i).setCellStyle(cellStyle);
            }

            // 각 셀의 width 지정해주기 - 행 5개니까 0 부터 5 까지
            for( int i = 0; i < row.getLastCellNum(); i++){
                sheet.setColumnWidth(i , 5000);
            }

            // 1000단위 쉼표로 바꿔주는 format -> 100,000
            DecimalFormat df = new DecimalFormat("###,###");

            // 총 금액 저장해주는 변수
            int totalPrice = 0;

            for (int i = 0; i < resoures.size(); i++) {

                // 엑셀은 LocalDatetime 으로 value 값 넣으면 숫자 이상하게 나온다
                // 문자열로 바꿔서 넣어줘야 정상적으로 나옴
                String clocaldate = resoures.get(i).getCdate().toLocalDate().toString();
                String ulocaldate = resoures.get(i).getUdate().toLocalDate().toString();

                // sheet 는 열
                row = sheet.createRow(rowno++);
                // index 번쨰 열 선언
                cell = row.createCell(0); // 상품명
                // 내가 원하는 열에 값 넣어주기
                cell.setCellValue(resoures.get(i).getResname());

                // 반복
                cell = row.createCell(1); // 수량
                cell.setCellValue(resoures.get(i).getPacount() + "개" );

                cell = row.createCell(2); // 개별가격
                cell.setCellValue( df.format( resoures.get(i).getResprice() ) + "원" );

                cell = row.createCell(3); // 총가격
                cell.setCellValue( df.format( ( resoures.get(i).getPacount() * resoures.get(i).getResprice() ) ) + "원" );

                cell = row.createCell(4); // 주문일
                cell.setCellValue(clocaldate);

                cell = row.createCell(5); // 도착일
                cell.setCellValue(ulocaldate.equals(clocaldate) ? "배송중" : ulocaldate );

                // 총 금액은 i 번째 정보들을 계산해서 누적 더하기 해준다
                totalPrice += resoures.get(i).getPacount() * resoures.get(i).getResprice();
            }
            // 마지막 열 선언 ++ 해줘야 다음 열로 넘어간다
            row = sheet.createRow(rowno++);
            cell = row.createCell(0);
            cell.setCellValue("총 금액 : " + df.format(totalPrice)  + " 원");

            // 셀 병합해주기                          마지막 열의 첫번째 행 , 마지막 행 0~5 까지 합치기
            sheet.addMergedRegion( new CellRangeAddress( rowno-1 , rowno-1 , 0 , 5 ) );

            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setContentType("ms-vnd/excel"); // 저장 타입                       // 파일 명 ( 한글은 오류남 )
            response.setHeader("Content-Disposition", "attachment;filename="+cdate+"_parcelLog.xlsx");

            // 다운로드 하기 -> 예외처리 해야되서 try catch
            workbook.write(response.getOutputStream());
            workbook.close();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }//Parcel end

    @GetMapping("/ResouresExcel")
    public void ResouresExcelDowunload(HttpServletResponse httpServletResponse){
        //** 주의!!!! axios는 blob 타입으로 해야함 아직 몰라요.... 그래서 호출시 a태그로 호출 바랍니다.
        try {
            //db 에서 resoures 전체 데이터 가져오기 !! 리스트로 가져오기
            List<ResouresDto> resouresList=inventoryService.ResouresExcel();
            System.out.println("resouresList = " + resouresList);

            //poi 라이브러리
            Workbook workbook = new XSSFWorkbook() ; //.xlsx 확장자용 객체
            Sheet sheet = workbook.createSheet("resouresList"); // sheet 이름 (한글날에 오류가능성이 있대)

            Row row; // 행
            Cell cell; //열
            int rowno = 0 ;

            // 숫자가 증가해야 옆 , 아래로 내려감
            row = sheet.createRow(rowno++);

            //index 번째 행 선언하기            0.재료번호 1.카테고리 2.재료이름 3.재료가격 4.재료개수
            cell = row.createCell(0);
            // 선언한 행에 value 넣기
            cell.setCellValue("재료번호");
            //반복하기
            cell = row.createCell(1);
            cell.setCellValue("카테고리");

            cell = row.createCell(2);
            cell.setCellValue("재료이름");

            cell = row.createCell(3);
            cell.setCellValue("재료가격");

            cell = row.createCell(4);
            cell.setCellValue("재료개수");

            //셀 스타일
            CellStyle cellStyle = workbook.createCellStyle();
            //셀 백그라운드 컬러
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            //채우기
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //폰트 스타일
            Font font = workbook.createFont();
            // 셀의 텍스트 가운데정렬

            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 가운데 정렬

            //bold
            font.setBold(true);
            cellStyle.setFont(font);
            // 각각의 행에 스타일 적용하기
            for(int i = 0 ; i< row.getLastCellNum() ; i++){
                row.getCell(i).setCellStyle(cellStyle);
            }
            // 각 셀의 width 지정해주기 - 행 5개니까 0 부터 5 까지
            for( int i = 0; i < row.getLastCellNum(); i++){
                sheet.setColumnWidth(i , 5000);
            }
            // 1000단위 쉼표로 바꿔주는 format -> 100,000
            DecimalFormat df = new DecimalFormat("###,###");
            Double totalresoure = 0.0;
            int rescount=0;                 // 재료의 개수를 반올림 하기 위한 코드
            for (int i = 0; i < resouresList.size(); i++) {
                 rescount= (int) Math.round(resouresList.get(i).getRescount());             // 재료를 반올림해서 정수로 표시해준다.
                // sheet 는 열
                row = sheet.createRow(rowno++);
                // index 번째 열 선언
                cell = row.createCell(0);
                //원하는 컬럼에 값 넣어주기  0.재료번호 1.카테고리 2.재료이름 3.재료가격 4.재료개수
                cell.setCellValue(resouresList.get(i).getResno());

                cell = row.createCell(1);
                cell.setCellValue(resouresList.get(i).getRescname());

                cell = row.createCell(2);
                cell.setCellValue(resouresList.get(i).getResname());


                cell = row.createCell(3);
                cell.setCellValue(df.format(resouresList.get(i).getResprice())+"원");


                cell = row.createCell(4);
                cell.setCellValue(rescount+"개");

                totalresoure += resouresList.get(i).getRescount();

            }
            //마지막 열 선언
            int roundedValue = (int) Math.round(totalresoure);   //총 재료의 개수를 정수로 반환해주는 코드
            row = sheet.createRow(rowno++);
            cell = row.createCell(0);
            cell.setCellValue("재료의총개수 : "+roundedValue + "개");

            //셀 병합
            sheet.addMergedRegion(new CellRangeAddress(rowno-1,rowno-1,0,5));
            httpServletResponse.setContentType("ms-vnd/excel"); // 저장 타입                       // 파일 명 ( 한글은 오류남 )
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=Resoures.xlsx");

            // 다운로드 하기 -> 예외처리 해야되서 try catch
            workbook.write(httpServletResponse.getOutputStream());
            System.out.println("다운로드실행");
            workbook.close();
            System.out.println("다운로드끝 닫힘");
        }//t end

        catch (Exception e){
            System.out.println("Excel오류 = " + e);
        }//c end
    }//r end
    @GetMapping("/logExcel")
    public void logExcel(@RequestParam String logdate,HttpServletResponse httpServletResponse){
        System.out.println("logdate = " + logdate);
        System.out.println("ExcelController.logExcel");
    try {

        Workbook workbook = new XSSFWorkbook(); // .xlsx 확장자용
        Sheet sheet = workbook.createSheet("inventoryLog"); // sheet 이름 (한글은 오류날 가능성 up)
        List<ResouresDto> logList= inventoryService.logExcel(logdate);
        if(logList!=null){

            Row row; // 행
            Cell cell; // 열
            int rowno = 0;


            // 숫자가 증가해야 옆 , 아래로 내려감
            row = sheet.createRow(rowno++);

            // index 번쨰 행 선언
            cell = row.createCell(0);
            // 선언한 행에 value 넣기
            cell.setCellValue("재고로그 번호");

            // 반복
            cell = row.createCell(1);
            cell.setCellValue("재고로그 날짜");

            cell = row.createCell(2);
            cell.setCellValue("재고로그 개수");

            cell = row.createCell(3);
            cell.setCellValue("재료 번호");

            cell = row.createCell(4);
            cell.setCellValue("재료 이름");

            // 셀 스타일 지정하기 위한 선언
            CellStyle cellStyle = workbook.createCellStyle();

            // 셀 백그라운드 컬러 - 연한회색( brown 으로 시도해봤지만 촌스러웠음 )
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            // 채우기
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 폰트 스타일
            Font font = workbook.createFont();
            // bold
            font.setBold(true);
            cellStyle.setFont(font);

            // 각각의 행에 스타일 적용하기
            for (int i = 0; i < row.getLastCellNum(); i++){
                row.getCell(i).setCellStyle(cellStyle);
            }


            // 각 셀의 width 지정해주기 - 행 5개니까 0 부터 5 까지
            for( int i = 0; i < row.getLastCellNum(); i++){
                sheet.setColumnWidth(i , 5000);
            }

            // 1000단위 쉼표로 바꿔주는 format -> 100,000
            DecimalFormat df = new DecimalFormat("###,###");

            Double totalPrices = 0.0;

            for(int i = 0 ; i<logList.size() ; i++ ){
                String clocaldate = logList.get(i).getInlogdate().toLocalDate().toString();

                //1. 재고 로그 번호 2. 재고로그 날짜 3. 재고로그 개수 4. 재료 번호 5. 재료 이름
                // sheet 는 열
                row = sheet.createRow(rowno++);
                // index 번쨰 열 선언
                cell = row.createCell(0); // 재고 로그 번호
                // 내가 원하는 열에 값 넣어주기
                cell.setCellValue(logList.get(i).getInlogno());

                cell = row.createCell(1); // 재고로그 날짜
                cell.setCellValue(clocaldate);

                cell = row.createCell(2); // 재고로그 개수
                cell.setCellValue(logList.get(i).getInloghistory());

                cell = row.createCell(3); // 재료 번호
                cell.setCellValue(logList.get(i).getResno());

                cell = row.createCell(4); // 재료 이름
                cell.setCellValue(logList.get(i).getResname());

                //판매한 총 금액을 구하기위한 코드 (만약 판매기록이 음수 ,판매된거면 그 개수에 값을 더해서 총 금액에 더해준다.)
                System.out.println("재고 로그 개수 = " + logList.get(i).getInloghistory());
                if(logList.get(i).getInloghistory()<0){
                    Double count = logList.get(i).getInloghistory()*-1;
                    System.out.println("count = " + count);
                    totalPrices  +=count * logList.get(i).getResprice();
                    System.out.println("count = " + count);
                }

            }
            //총 금액을 반올림 하여 int 형으로 변환해준다.
            int totalPrice =(int)Math.round(totalPrices);
            System.out.println("totalPrice = " + totalPrice);

            // 마지막 열 선언 ++ 해줘야 다음 열로 넘어간다
            row = sheet.createRow(rowno++);
            cell = row.createCell(0);
            cell.setCellValue("총 판매 : " + df.format(totalPrice)  + " 원");

        }
        System.out.println("logList = " + logList);
        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setContentType("ms-vnd/excel"); // 저장 타입                       // 파일 명 ( 한글은 오류남 )
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename="+logdate+"_inventoryLog.xlsx");

        // 다운로드 하기 -> 예외처리 해야되서 try catch
        workbook.write(httpServletResponse.getOutputStream());
        workbook.close();

    }
        catch (Exception e){
            System.out.println("logExcel = " + e);
        }



    }

    // 상품 엑셀
    @GetMapping("/product")
    public void productExcelDownload(HttpServletResponse response) {
        try {

            // db 에서 꺼내올 데이터 저장할 리스트
            List<ProductDto> productDtoList = productService.getProduct(0, "", "pno");
            System.out.println("productDtoList = " + productDtoList);

            // poi 라이브러리의 Workbook
            // ms오피스의 확장자를 사용 할 수 있게 해줌
            Workbook workbook = new XSSFWorkbook(); // .xlsx 확장자용
            Sheet sheet = workbook.createSheet("productexcel"); // sheet 이름 (한글은 오류날 가능성 up)

            Row row; // 행
            Cell cell; // 열
            int rowno = 0;

            // 숫자가 증가해야 옆 , 아래로 내려감
            row = sheet.createRow(rowno++);

            // index 번쨰 행 선언
            cell = row.createCell(0);
            // 선언한 행에 value 넣기
            cell.setCellValue("상품명");

            // 반복
            cell = row.createCell(1);
            cell.setCellValue("상품가격");

            cell = row.createCell(2);
            cell.setCellValue("상품카테고리");

            // 셀 스타일 지정하기 위한 선언
            CellStyle cellStyle = workbook.createCellStyle();

            // 셀 백그라운드 컬러 - 연한회색
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            // 채우기
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 폰트 스타일
            Font font = workbook.createFont();
            // bold
            font.setBold(true);
            cellStyle.setFont(font);

            // 각각의 행에 스타일 적용하기
            for (int i = 0; i < row.getLastCellNum(); i++){
                row.getCell(i).setCellStyle(cellStyle);
            }

            // 각 셀의 width 지정해주기 - 행 2개니까 0 부터 2 전 까지
            for( int i = 0; i < row.getLastCellNum(); i++){
                sheet.setColumnWidth(i , 5000);
            }

            // 1000단위 쉼표로 바꿔주는 format -> 100,000
            DecimalFormat df = new DecimalFormat("###,###");



            for (int i = 0; i < productDtoList.size(); i++) {

                // sheet 는 열
                row = sheet.createRow(rowno++);
                // index 번쨰 열 선언
                cell = row.createCell(0); // 상품명
                // 내가 원하는 열에 값 넣어주기
                cell.setCellValue(productDtoList.get(i).getPname());

                // 반복
                cell = row.createCell(1); // 상품가격
                cell.setCellValue(df.format(productDtoList.get(i).getPprice()) + "원");

                cell = row.createCell(2); // 상품카테고리
                cell.setCellValue(productDtoList.get(i).getPcname());

            }

            response.setContentType("ms-vnd/excel"); // 저장 타입
            response.setHeader("Content-Disposition", "attachment;filename="+ LocalDateTime.now() +"_productexcel.xlsx");


            // 다운로드 하기 -> 예외처리 해야되서 try catch
            workbook.write(response.getOutputStream());
            workbook.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    @GetMapping("/sales")
    public void salesExcelDownload(HttpServletResponse response) {
        System.out.println("ExcelController.salesExcelDownload");
        try{

            List<OrdersDto> salesList = saleService.getOrderListExcelDownload();


            System.out.println("salesList = " + salesList);

            // poi 라이브러리의 Workbook
            // ms오피스의 확장자를 사용 할 수 있게 해줌
            Workbook workbook = new XSSFWorkbook(); // .xlsx 확장자용
            Sheet sheet = workbook.createSheet("sales"); // sheet 이름 (한글은 오류날 가능성 up)

            Row row; // 행
            Cell cell; // 열
            int rowno = 0;

            // 숫자가 증가해야 옆 , 아래로 내려감
            row = sheet.createRow(rowno++);

            // index 번쨰 행 선언
            cell = row.createCell(0);
            // 선언한 행에 value 넣기
            cell.setCellValue("주문번호");

            // 반복
            cell = row.createCell(1);
            cell.setCellValue("주문회원번호");

            cell = row.createCell(2);
            cell.setCellValue("주문상태");

            cell = row.createCell(3);
            cell.setCellValue("주문일");

            cell = row.createCell(4);
            cell.setCellValue("주문금액");

            cell = row.createCell(5);
            cell.setCellValue("결제금액");

            // 셀 스타일 지정하기 위한 선언
            CellStyle cellStyle = workbook.createCellStyle();

            // 셀 백그라운드 컬러 - 연한회색( brown 으로 시도해봤지만 촌스러웠음 )
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            // 채우기
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 폰트 스타일
            Font font = workbook.createFont();
            // bold
            font.setBold(true);
            cellStyle.setFont(font);

            // 각각의 행에 스타일 적용하기
            for (int i = 0; i < row.getLastCellNum(); i++){
                row.getCell(i).setCellStyle(cellStyle);

            }

            // 각 셀의 width 지정해주기 - 행 5개니까 0 부터 5 까지
            for( int i = 0; i < row.getLastCellNum(); i++){
                sheet.setColumnWidth(i , 5000);
            }

            // 1000단위 쉼표로 바꿔주는 format -> 100,000
            DecimalFormat df = new DecimalFormat("###,###");

            // 총 금액 저장해주는 변수
            int totalPrice = 0;
            LocalDate date = LocalDate.now();

            // 헤더 말고 데이터 셀이 가운데 정렬 (셀 스타일 지정하기 위한 선언)
            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < salesList.size(); i++) {

                // 엑셀은 LocalDatetime 으로 value 값 넣으면 숫자 이상하게 나온다
                // 문자열로 바꿔서 넣어줘야 정상적으로 나옴
                String odate = salesList.get(i).getOdate().toLocalDate().toString();

                // sheet 는 열
                row = sheet.createRow(rowno++);
                // index 번쨰 열 선언
                cell = row.createCell(0); // ("주문번호");
                // 내가 원하는 열에 값 넣어주기
                cell.setCellValue(salesList.get(i).getOno());

                // 반복
                cell = row.createCell(1); // ("주문회원번호");
                cell.setCellValue( salesList.get(i).getMno() );

                cell = row.createCell(2); // ("주문상태");
                cell.setCellValue( salesList.get(i).getOstate() == 0 ? "결제완료" : "환불");

                cell = row.createCell(3); // ("주문일");
                cell.setCellValue( odate );

                cell = row.createCell(4); // ("주문금액");
                cell.setCellValue( df.format(  salesList.get(i).getTotalprice() ));

                cell = row.createCell(5); // ("결제금액");
                cell.setCellValue(df.format(  salesList.get(i).getPaid() ) );

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // 행의 각 셀에 데이터 스타일 적용
                    row.getCell(j).setCellStyle(dataCellStyle);
                }

                // 총 금액은 i 번째 정보들을 계산해서 누적 더하기 해준다
                totalPrice += salesList.get(i).getPaid();

            }


            // 마지막 열 선언 ++ 해줘야 다음 열로 넘어간다
            row = sheet.createRow(rowno++);
            cell = row.createCell(0);
            cell.setCellValue("총 결제 금액 : " + df.format(totalPrice)  + " 원");

            // 셀 병합해주기                          마지막 열의 첫번째 행 , 마지막 행 0~5 까지 합치기
            sheet.addMergedRegion( new CellRangeAddress( rowno-1 , rowno-1 , 0 , 5 ) );

            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setContentType("ms-vnd/excel"); // 저장 타입                       // 파일 명 ( 한글은 오류남 )
            response.setHeader("Content-Disposition", "attachment;filename="+date+"sales.xlsx");

            // 다운로드 하기 -> 예외처리 해야되서 try catch
            workbook.write(response.getOutputStream());
            workbook.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }

}

