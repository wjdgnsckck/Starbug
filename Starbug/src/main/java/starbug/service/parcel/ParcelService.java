package starbug.service.parcel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import starbug.controller.parcel.AlramController;
import starbug.controller.parcel.ParcelController;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.entity.inventory.InventoryLogEntity;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.entity.resoures.RescategoryEntity;
import starbug.model.entity.resoures.ResouresEntity;
import starbug.model.repository.resoures.Inventory_LogEntityRepository;
import starbug.model.repository.resoures.ParcellogEntityRepository;
import starbug.model.repository.resoures.RescategoryEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelService {

    @Autowired
    private AlramController alramController;

    @Autowired
    private ParcellogEntityRepository parcelRepository;

    @Autowired
    private ResouresEntityRepository resouresEntityRepository;

    @Autowired
    private RescategoryEntityRepository rescategoryEntityRepository;

    @Autowired
    private Inventory_LogEntityRepository inventoryLogEntityRepository;

    // 날짜별로 발주 내역 확인 get
    @Transactional
    public List<ResouresDto> getParcels( String cdate ){ // 발주로그 출력 Service

        // parcelRepository 통해서 cdate 로 발주내역 찾아서 엔티티에 담기
        List<ParcellogEntity> resouresEntities = parcelRepository.findByPacelLogAndResoures( cdate );

        List<ResouresDto> resouresDtos = new ArrayList<>();

        // 엔티티 갯수만큼 List 에 담는다.
        resouresEntities.forEach( e -> {
            resouresDtos.add( e.toParcellogDto() );
        });

        return resouresDtos;
    }
    
    
    // 발주 할 떄 발주 등록
    @Transactional
    public boolean postParcel( ResouresDto resouresDto ){ // 발주 할때 DB 등록

        // 유효성 검사는 하지 않는다 save 되면 true 반환
        ParcellogEntity parcellogEntity =
                parcelRepository.save( resouresDto.toParcellogEntity() );

        // 엔티티로 save 된 pano 가 0보다 크면 = db 저장 성공 했으면
        if(parcellogEntity.getPano() > 0 ){
            return true;
        }
        return false;
    }


    // 발주 도착용 날짜 수정함수
    //@Async <- 멀티스레드용 비동기 실행 어노테이션
    @Transactional
    public boolean putParcel( int pano ){ // 발주 도착 Service

        // 도착 시간 = 현재시간
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("localDateTime = " + localDateTime );

        // view 에서 도착 완료 신청한 레코드 검색
        Optional<ParcellogEntity> optionalParcellog =
                parcelRepository.findById( pano ); // view 에서 보낸 pano

        System.out.println("optionalParcellog = " + optionalParcellog );

        // pano 로 찾은 엔티티 있으면
        if( optionalParcellog.isPresent() ){

            //엔티티 객체에 담아준다
            ParcellogEntity parcellogEntity = optionalParcellog.get();

            // 주문 날짜
            LocalDateTime cdate = parcellogEntity.getCdate();
            // 도착날짜
            LocalDateTime udate = parcellogEntity.getUdate();
                // 주문 날짜 == 도착 날짜 -> 도착 처리 안된 엔티티
            if(cdate.equals(udate)){
                // udate(도착날짜) 만 수정
                parcellogEntity.setUdate( localDateTime );

                return true;
            }
        }
        return false;
    }

    // 환불용 삭제 함수 - 주문 취소용(도착 안했을때)
    @Transactional
    public boolean deleteParcel( int pano ){ // 발주 시킨거 환불 Service

        // 선택된 prno 로 환불할 발주 검색
        Optional<ParcellogEntity> parcellogEntity = parcelRepository.findById( pano );

        if( parcellogEntity.isPresent() ){
            // 삭제한다.
            parcelRepository.deleteById( pano );

            return true;
        }

        return false;
    }

    // 발주 위해 재료 목록 불러오기
    public List<ResouresDto> getResoures( int rescno ){ // 재료 발주 출력용으로 불러오기

        List<ResouresEntity> resouresEntities =
                resouresEntityRepository // 재료 , 재료카테고리 , 거래처 join 한거 resno 오름차순으로 출력
                        .findAllByResourcesAndRescategoryAndProducerAscResno( rescno );
        // List 로 받은 엔티티 들 저장할 Dto
        List<ResouresDto> resouresDtos = new ArrayList<>();

        //엔티티 길이만큼 반복해서 dto에 담는다
        resouresEntities.forEach( e -> {
            // List 에 담와서 반환한다.
            resouresDtos.add( e.toAllResouresDtos() );

        });
        System.out.println( "resouresDtos = " + resouresDtos );
        return resouresDtos;
    }

    // 위에 있는 카테고리 join 은 출력할때 카테고리 구분위해 join
    // 해당 함수는 화면에 출력용
    // 재료 목록 구분하기 위해 카테고리 불러오기
    public List<ResouresDto> getResouresCategory(){ // 재료 카테고리 출력용으로 불러오기

        List<RescategoryEntity> rescategoryEntities = // 카테고리 번호 오름차순으로 출력 // 그대로 불러오면 뒤죽박죽
                rescategoryEntityRepository.findAll(Sort.by(Sort.Order.asc("rescno")) );

        // List 로 받은 엔티티들 저장할 Dto
        List<ResouresDto> rescategoryDtos = new ArrayList<>();

        rescategoryEntities.forEach( e -> {

            rescategoryDtos.add( e.toRescategoryDto() );

        });


        return rescategoryDtos;
    }

    // 주문한 날짜 출력하는 함수
    // distinct로 중복제거 했지만
    // 초 , 시간 별로 다를 수 있기 때문에 view 쪽에서 한번 더 거를 예정
    public List<ResouresDto> getDateList(){
        // 올해 것만 출력 할수 있게 하기 위함 ex) 2023 만
        LocalDate localDate = LocalDate.now();
        // 날짜중 year 만 뺴와서 String 으로 변환
        String nowYear = Integer.toString( localDate.getYear() );

        System.out.println( "nowYear = " + nowYear );

        // 2023 년 주문 중 중복된 날짜는 제거해서 출력
        List<ParcellogEntity> parcellogEntities =
                parcelRepository.findByCdateDistinctCdate(nowYear);
        System.out.println( "parcellogEntities = " + parcellogEntities );

        List<ResouresDto> resouresDtos = new ArrayList<>();

        parcellogEntities.forEach( e -> {

           resouresDtos.add( e.toParcellogDto() );
        });

        System.out.println("resouresDtos = " + resouresDtos);
        
        return resouresDtos;
    }

    // 환불 신청만 해주는 함수 pstate 1로 바꿔줄거임
    @Transactional
    public boolean refundParcel( int pano ){

        // 환불 신청한 발주 로그 검색해서 Optional 에 저장
        Optional<ParcellogEntity> optionalParcellog =
                parcelRepository.findById(pano);

        if(optionalParcellog.isPresent()){

            // 찾았으면 엔티티에 저장하고
            ParcellogEntity parcellogEntity = optionalParcellog.get();
            // 상태를 true (1)로 바꿔준다 -> 추후에 스케쥴러로 환불 처리 해야하기 떄문
            parcellogEntity.setPastate( true );
            System.out.println( parcellogEntity );
            return true;
        }

        return false;
    }


    // 스케쥴러 메서드 매일 17시에 실행( 매일 17시에 view에서 신청한 환불 진행 )
    @Transactional
    @Scheduled(cron = "0 55 13 * * *")
    public void schedulRefund() throws Exception {

        // 상태(pastate) 가 true(1) 인 엔티티들 검색 여러개일 수 있기 때문에 List 로 받는다.
        List<ParcellogEntity> parcellogEntityList =
                parcelRepository.findByPastate(true);
        System.out.println("스케쥴러 재고로그 : " + parcellogEntityList);

        // 찾은 엔티티들의 pano 들을 저장할 List
        List<Integer> panoList = new ArrayList<>();

        // 찾은 엔티티의 resno 저장할 List -> 소켓용
        List<Integer> resnoList = new ArrayList<>();

        // 찾은 엔티티의 상품 이름 저장할 리스트 -> 소켓용
        List<String> resnameList = new ArrayList<>();

        if( parcellogEntityList != null ){
            System.out.println("if 문 들어왔다");

            // 엔티티 길이만큼 배열에 저장 (pano 만)
            parcellogEntityList.forEach( r->{
                panoList.add(r.getPano());
                resnoList.add(r.getResouresEntity().getResno());
            }); // forEach end

            System.out.println("resnoList : " + resnoList);

            // resnameList 에 재료 이름 넣기
            for( int i = 0; i < resnoList.size(); i++ ){
                resnameList.add(
                        resouresEntityRepository
                                .findById( resnoList.get(i) )
                                .get()
                                .getResname()
                );
            } // for end

            // 한번에 삭제가 안되기 때문에 for 문으로 하나씩 삭제한다 -> 한번에 삭제하려면 ResultSet 오류남
            for(int i = 0 ; i<panoList.size(); i ++){
                parcelRepository.deleteById(panoList.get(i));

            }
            List<WebSocketSession> sessions = alramController.getResouresList();

            TextMessage message = new TextMessage( "신청하신 상품들 : "+resnameList+"\n환불처리가 완료되었습니다. 페이지를 새로고침 해주세요");

            for( WebSocketSession session : sessions ){
                if( session.isOpen()){
                    alramController.handleMessage(session , message);
                }
            } // for end
        }// if end
    }// 스케쥴러 end

    // 재고 로그의 가장 최근 발주 목록 출력
    @Transactional
    public List<ResouresDto> todayGetParcel(){
        System.out.println("todayGetParcel 들어오니..");
        // 오늘날짜
        LocalDate localDate = LocalDate.now();

        // 오늘 날짜의 발주 목록 검색
        List<ParcellogEntity> parcellogEntities =
                parcelRepository.findByCdate(localDate);

        System.out.println( "오늘 날짜의 발주 목록 검색" + parcellogEntities );

        // 엔티티를 담아줄 리스트 선언
        List<ResouresDto> resouresDtoList = new ArrayList<>();

        // 오늘 날짜의 발주로그 있으면
        if( !parcellogEntities.isEmpty()){

            System.out.println("오늘 날짜의 발주로그 있으면");

            parcellogEntities.forEach( r ->{
                // 리스트에 담기
                resouresDtoList.add( r.toParcellogDto() );

            });
            System.out.println( "resouresDtoList : " + resouresDtoList);
        }
        else { // 오늘 발주한거 없으면

            System.out.println("오늘 발주한거 없으면");
            // 가장 최근의 발주한 날짜 가져오기
            Optional<ParcellogEntity> maxCdateEntity =
                    parcelRepository.findGetMaxCdate();

            // cdate 는 LocalDateTime 형식 -> 검색 위해서 LocalDate 로 바꿔준다.
            LocalDate cdate = maxCdateEntity.get().getCdate().toLocalDate();

            System.out.println( "todayGetParcel 의 cdate : " + cdate );

            // 추출한 가장 최근 발주한 날짜로 다시 검색
            List<ParcellogEntity> parcellogYesterDay =
                    parcelRepository.findByCdate(cdate);

            // 검색 내역 있으면
            if(parcellogYesterDay != null){

                parcellogYesterDay.forEach( r -> {
                    resouresDtoList.add( r.toParcellogDto() );
                });
            }
        }


        return resouresDtoList;
    }
   /* @Async // 멀티 스레드 구현해보려 했으나 실패했음.. 아까워서 주석 처리만 합니다...
    public void putParcelStart(LocalDate localDate) {

        try{
            // 스레드 멈추기 10초 1000 = 1초
            Thread.sleep(10000);
        }
        catch (Exception e){
            System.out.println("스레드 오류 메세지 : " + e.getMessage());
        }
        // 10초 멈추고 나면 putParcel 실행
        // 서비스 로직만 실행할수 없고
        // controller 로직 호출하면 순환오류
        // controller에 멀티 스레드 구현하면 동기로 실행됨 -> 같은 클래스에 있어서 -> 그래서 구현 실패
        putParcel(localDate);

    }*/


   /*
    // 재료의 개수를 구하기 위한 메소드!! 재고 관리시 필요합니다 -정훈-
    @Transactional
    public int getpParcelCount(int resno){
        // resno[FK] 에 대한 엔티티
        Optional<ResouresEntity> resouresEntityObject = resouresEntityRepository.findById( resno );
        if( resouresEntityObject.isPresent() ){

            // 해당 재료번호의 모든 발주 목록
            resouresEntityObject.get().getParcellogEntities();


        }
        return 1;

    }


    @Autowired
    ResouresEntityRepository resouresEntityRepository;

 */
}
