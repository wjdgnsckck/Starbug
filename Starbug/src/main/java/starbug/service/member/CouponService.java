package starbug.service.member;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import starbug.model.dto.member.CouponHistoryDto;
import starbug.model.dto.member.StarBugMemberDto;
import starbug.model.dto.page.PageDto;
import starbug.model.entity.member.CouponEntity;
import starbug.model.entity.member.CouponHistoryEntity;
import starbug.model.entity.member.StarBugMemberEntity;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.repository.member.CouponEntityRepository;
import starbug.model.repository.member.CouponHistoryEntityRepository;
import starbug.model.repository.member.StarBugMemberEntityRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CouponService {
    @Autowired
    private CouponEntityRepository couponEntityRepository;

    @Autowired
    private CouponHistoryEntityRepository couponHistoryEntityRepository;

    @Autowired
    private StarBugMemberEntityRepository starBugMemberEntityRepository;

    @Autowired
    private MessageSendingService messageSendingService;


    // 쿠폰 카테고리 등록
    @Transactional
    public boolean couponWrite(CouponHistoryDto couponHistoryDto){
        //System.out.println("서비스 couponHistoryDto : "+couponHistoryDto);
        // 쿠폰Dto 데이터들을 쿠폰(카테고리)엔티티에 저장
        CouponEntity couponEntity = couponEntityRepository.save(couponHistoryDto.couponToEntity());
        if (couponEntity.getCcno() >= 1){return true;}
        return false;
    }

    // 쿠폰 카테고리 출력
    @Transactional
    public PageDto couponGet(int page , int view){
        // 1. 모든게시물 호출
        // Sort로 "ccno"필드 기준으로 내림차순
        Pageable pageable = PageRequest.of(page-1 , view , Sort.Direction.DESC ,"ccno");

        Page<CouponEntity> couponEntities = couponEntityRepository.findAll(pageable);
        // dto로 변환
        List<CouponHistoryDto> couponHistoryDtos = new ArrayList<>();
        couponEntities.forEach( e ->{
            couponHistoryDtos.add(e.couponToDto());
        });

        // 총페이지수
        int totalPages = couponEntities.getTotalPages();
        // 총게시물수
        Long totalCount = couponEntities.getTotalElements();
        PageDto pageDto = PageDto.builder()
                .couponDtos(couponHistoryDtos)
                .totalPage(totalPages)
                .totalCount(totalCount)
                .build();

        return pageDto;
    }

    // 쿠폰 카테고리 삭제 (회원이 쿠폰 다 썼을때만 삭제 가능)
    @Transactional
    public boolean couponDelete(int ccno){
        //System.out.println("서비스 쿠폰 삭제 "+ccno);
        Optional<CouponEntity> optionalEntity = couponEntityRepository.findById(ccno);
        if (optionalEntity.isPresent()) {
            couponEntityRepository.deleteById(ccno);
            return true;
        }
        return false;
    }

    // 쿠폰 회원에게 발급 + 문자보내기
    @Transactional
    public boolean memberCouponw(int cno , List<String> mnoList ){ //
        //System.out.println("서비스 cno :"+cno);
        // 메세지 보낼 회원들 정보와 메세지 내용 리스트를 담을 객체 선언
        List<Map<String,String>> messages = new ArrayList<Map<String,String>>();

        for(String mno : mnoList){  //mnoList의 길이만큼 for문 실행
            // 유효성을 위해 mno로 회원 엔티티 찾아보기
            Optional<StarBugMemberEntity> starBugMemberoptionalEntity
                    = starBugMemberEntityRepository.findById(Integer.parseInt(mno));

            if (starBugMemberoptionalEntity.isPresent()){   // 있는 회원이면
                //System.out.println(" 서비스엔티티 "+starBugMemberoptionalEntity);
                // 회원 엔티티에 저장
                StarBugMemberEntity starBugMemberEntity = starBugMemberoptionalEntity.get();

                Optional<CouponEntity> couponEntityOptional     // 쿠폰카테고리 번호로 쿠폰카테고리 찾아보기
                        = couponEntityRepository.findById(cno);
                if (couponEntityOptional.isPresent()) {  // 쿠폰카테고리가 존재하면
                    //System.out.println("서비스엔티티쿠폰 " + couponEntityOptional);
                    // 쿠폰 엔티티에 쿠폰카테고리 저장
                    CouponEntity couponEntity = couponEntityOptional.get();

                    CouponHistoryEntity couponHistoryEntity =
                            CouponHistoryEntity.builder()
                                    // 문자열 형식 변환 날짜 - 카테고리번호 - 회원번호 로 저장
                                    .chcode(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)+"-"
                                            +String.format("%03d", cno)+"-"
                                            +String.format("%03d", Integer.parseInt(mno)))
                                    .chstate(0)
                                    .starBugMemberEntity(starBugMemberEntity)
                                    .couponEntity(couponEntity)
                                    .build();

                    //System.out.println("서비스 히스토리 엔티티 " + couponHistoryEntity);
                    // CouponHistoryEntity 저장
                    couponHistoryEntityRepository.save(couponHistoryEntity);

                    // --- 양방향 저장
                    // 1. 회원엔티티에 쿠폰히스토리내역(list) 넣어주기
                    starBugMemberEntity.getCouponHistoryEntities().add(couponHistoryEntity);
                    // 2. 쿠폰엔티티에 쿠폰히스토리내역(list) 넣어주기
                    couponEntity.getCouponHistoryEntities().add(couponHistoryEntity);

                    // -------- coolsms로 메세지 보내기 -------- //
                    // 문자 내용 데이터 변수에 저장
                    String messageText = starBugMemberEntity.getMname()
                            + "님, " + couponEntity.getCccontent()
                            + " 쿠폰이 발급되었습니다. -커피프린스 1호점-";
                    //사용기한은 " + couponEntity.getCcdate() + "일입니다.
                    //회원연락처 변수에 저장 양식은 번호만 받아야기 때문에 "-" 생략
                    String memberphone =
                            starBugMemberEntity.getMphone().split("-")[0]
                            + starBugMemberEntity.getMphone().split("-")[1]
                            + starBugMemberEntity.getMphone().split("-")[2];
                    //System.out.println("memberphone: " + memberphone);
                    //System.out.println("messageText"+messageText);

                    // 회원 1명의 연락처와 문자내용을 담아둘 map 선언
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("memberphone" , memberphone);
                    maps.put("messageText" , messageText);
                   // System.out.println("maps"+maps);
                    messages.add(maps); // 상단에 선언해둔 list에 회원1명의 데이터저장
                    //System.out.println("메세지보낼때 넘기는 객체리스트 : "+messages);
                } else {
                    // 쿠폰이 존재하지 않는 경우
                    return false;
                }
            } else {
                // 회원이 존재하지 않는 경우
                return false;
            }
        }
        //messageSendingService 클래스로 호출하여 매개변수전달
        messageSendingService.sendSMS( messages );
        return true;
    }


    // 스케쥴러 메서드 매일 00시에 실행( 매일 00시에 쿠폰사용기한을 비교한 후 쿠폰 상태 변경 )
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void schedulCoupon(){

        // 상태(chstate) 가 true(0) 인 엔티티들 검색 여러개일 수 있기 때문에 List 로 받는다.
        List<CouponHistoryEntity> couponHistoryEntities =
                couponHistoryEntityRepository.findByStateCoupon(0);

        System.out.println("스케쥴러 쿠폰엔티티 : "+couponHistoryEntities);

        // 찾은 엔티티들의 chisno 들을 저장할 List
        List<Integer> chisnoList = new ArrayList<>();

        if( couponHistoryEntities != null ){
            System.out.println("if 문 들어왔다");

            // 엔티티 길이만큼 배열에 저장 (chisno 만)
            couponHistoryEntities.forEach( r->{
                chisnoList.add(r.getChisno());
            });

            // for문으로 하나씩 상태 변경 해준다. -> 한번에 삭제하려면 ResultSet 오류남
            for(int i = 0 ; i<chisnoList.size(); i ++){
                Optional<CouponHistoryEntity> optionalEntity
                        = couponHistoryEntityRepository.findById(chisnoList.get(i));    // List에 담긴 쿠폰번호만 조회
                if(optionalEntity.isPresent()){ // 있는 쿠폰번호이면
                    CouponHistoryEntity couponHistoryEntity = optionalEntity.get(); // 엔티티에 있는 데이터를 꺼냄
                    couponHistoryEntity.setChstate(-1);  // 상태를 -1로 변경해준다
                }
            }
        }

    }
}
