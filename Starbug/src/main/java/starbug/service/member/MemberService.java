package starbug.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import starbug.model.dto.member.CouponHistoryDto;
import starbug.model.dto.member.StarBugMemberDto;
import starbug.model.dto.page.PageDto;
import starbug.model.entity.member.CouponEntity;
import starbug.model.entity.member.CouponHistoryEntity;
import starbug.model.entity.member.StarBugMemberEntity;
import starbug.model.repository.member.CouponHistoryEntityRepository;
import starbug.model.repository.member.StarBugMemberEntityRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private CouponHistoryEntityRepository couponHistoryEntityRepository;
    @Autowired
    private StarBugMemberEntityRepository starBugMemberEntityRepository;

    // 회원 등록
    @Transactional
    public boolean memberWrite(StarBugMemberDto starBugMemberDto){
        //System.out.println("starBugMemberDto = " + starBugMemberDto);
        StarBugMemberEntity starBugMemberEntity = starBugMemberEntityRepository.save(starBugMemberDto.memberToEntity());
        if (starBugMemberEntity.getMno() >= 1){return true;}
        return false;
    }

    // 모든 회원 조회
    @Transactional
    public PageDto getAll( int page , String key , String keyword , int view){

        // 페이징처리위한 인터페이스 사용
        Pageable pageable = PageRequest.of(page-1 , view , Sort.Direction.DESC ,"mno");
        // 1. 모든게시물 호출
        // Sort로 "mno"필드 기준으로 검색후 내림차순 - 페이징처리하기 전 코드
        //List<StarBugMemberEntity> starBugMemberEntities = starBugMemberEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "mno"));
        Page<StarBugMemberEntity> starBugMemberEntities = starBugMemberEntityRepository.findByMserch(key , keyword , pageable );
        // entity -> dto 변환
        // list 객체에 선언후 담기
        List<StarBugMemberDto> starBugMemberDtos = new ArrayList<>();
        starBugMemberEntities.forEach( e ->{
            starBugMemberDtos.add(e.memberToDto()); // dto로 변환
        });
        //System.out.println("회원조회 서비스 : "+starBugMemberDtos);
        // 총페이지수
        int totalPages = starBugMemberEntities.getTotalPages();
        // 총게시물수
        Long totalCount = starBugMemberEntities.getTotalElements();
        // Dto로 변환
        PageDto pageDto = PageDto.builder()
                .membersDtos(starBugMemberDtos)
                .totalPage(totalPages)
                .totalCount(totalCount)
                .build();
       // System.out.println("서비스 :"+pageDto);
        return pageDto;
    }


    // 회원 1명 상세 조회
    @Transactional
    public StarBugMemberDto getInfo(int mno){
        //System.out.println("서비스 mno : "+mno);
        // mno회원번호pk를 이용해 엔티티 찾기
        Optional<StarBugMemberEntity> starBugMemberEntityOptional = starBugMemberEntityRepository.findById(mno);
        // starBugMemberEntityOptional 안에 엔티티가있으면
        if (starBugMemberEntityOptional.isPresent()){
            // Optional 엔티티꺼내서 starBugMemberEntity에 넣기
            StarBugMemberEntity starBugMemberEntity = starBugMemberEntityOptional.get();

            // 쿠폰 내역 dto 변환 list이기때문에 map함수 사용
            List<CouponHistoryDto> couponHistoryDtos = starBugMemberEntity.getCouponHistoryEntities().stream()
                    .map(couponHistoryEntity ->
                        CouponHistoryDto.builder()
                                .chisno(couponHistoryEntity.getChisno())        //쿠폰히스토리번호
                                .cdate(couponHistoryEntity.getCdate())          //쿠폰발급일
                                .chcode(couponHistoryEntity.getChcode())        //쿠폰코드
                                .chstate(couponHistoryEntity.getChstate())      //쿠폰상태
                                .cccontent(couponHistoryEntity.getCouponEntity().getCccontent())    // 쿠폰내용
                                .ccpercent(couponHistoryEntity.getCouponEntity().getCcpercent())    // 할인율
                                .ccdate(couponHistoryEntity.getCouponEntity().getCcdate())  // 쿠폰기한
                                .build()
                    )
                    .collect(Collectors.toList());

            // StarBugMemberDto를 빌더 패턴으로 생성후 반환
            return StarBugMemberDto.builder()
                    .mno(starBugMemberEntity.getMno())
                    .mname(starBugMemberEntity.getMname())
                    .mage(starBugMemberEntity.getMage())
                    .msex(starBugMemberEntity.getMsex())
                    .mdate(starBugMemberEntity.getMdate())
                    .metc(starBugMemberEntity.getMetc())
                    .mphone(starBugMemberEntity.getMphone())
                    .couponHistoryDtos(couponHistoryDtos)   // couponHistoryDtos를 dto 필드에 선언후 담아줌
                    .build();
        }
        return null;
    }

    // 회원 수정
    @Transactional
    public boolean memberUpdate( StarBugMemberDto starBugMemberDto ){
        //System.out.println("서비스 : "+starBugMemberDto);
        Optional<StarBugMemberEntity> optionalEntity
                = starBugMemberEntityRepository.findById(starBugMemberDto.getMno());    // dto에 담긴 회원번호를 조회
        if(optionalEntity.isPresent()){ // 있는 회원번호이면
            StarBugMemberEntity starBugMemberEntity = optionalEntity.get(); // 엔티티에 있는 데이터를 꺼냄
            starBugMemberEntity.setMname(starBugMemberDto.getMname());  // dto에 있는 데이터를 엔티티 각 필드에 맞게 저장
            starBugMemberEntity.setMage(starBugMemberDto.getMage());
            starBugMemberEntity.setMetc(starBugMemberDto.getMetc());
            starBugMemberEntity.setMphone(starBugMemberDto.getMphone());
            return true;
        }
        return false;
    }

    // 회원 삭제
    @Transactional
    public boolean memberDelete(int mno){
        System.out.println("회원서비스 mno: "+mno);
        Optional<StarBugMemberEntity> optionalEntity = starBugMemberEntityRepository.findById(mno); // 회원번호 조회
        if (optionalEntity.isPresent()) { // 있는 회원번호이면
            StarBugMemberEntity starBugMemberEntity = optionalEntity.get(); // 엔티티에 있는 데이터를 꺼냄
            starBugMemberEntity.setMname("탈퇴한 회원");  // 회원 이름을 "탈퇴한 회원"으로 저장
            starBugMemberEntity.setMage(0);             // 회원 나이 0
            starBugMemberEntity.setMetc("탈퇴한 회원");  // 회원 정보 "탈퇴한 회원"으로 저장
            starBugMemberEntity.setMphone("X");         // 회원연락처는 "X"로 저장
            // 성별과 가입했던 날은 그냥 두겠습니다.
            return true;
        }
        return false;
    }
}
