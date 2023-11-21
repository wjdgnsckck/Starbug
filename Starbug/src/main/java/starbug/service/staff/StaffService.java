package starbug.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.staff.StaffListDto;
import starbug.model.entity.staff.StaffListEntity;
import starbug.model.repository.staff.StaffListEntityRepository;
import starbug.service.FileService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService implements  UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Autowired
    private StaffListEntityRepository staffListEntityRepository;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private FileService fileService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 인사 등록
    @Transactional
    public boolean staffPost(StaffListDto staffListDto) {

        System.out.println("★★★★★★첨부파일 확인 name :" + staffListDto.getFile().getName());
/*
        // 암호화 서비스를 통해서 주민번호 뒷자리 암호화 후 dto 대입
        String encoderIdnum =encryptService.encryptIdnum(staffListDto.getSidnum2());
        staffListDto.setSidnum2(encoderIdnum);*/

        // 주민번호 뒷자리 암호화 해서 dto에 대입
        staffListDto.setSidnum2(passwordEncoder.encode(staffListDto.getSidnum2()));

        // 저장할 데이터를 staffListEntityRepository 통해서 entity 에 save
        StaffListEntity entity = staffListEntityRepository.save(staffListDto.toEntity());



       // 반환 된 sno 가 1 이상이라면(등록성공) true 반환
        if(entity.getSno() >=1){
            // 인사정보 등록 성공시 파일 처리 진행
            String filename = fileService.fileUpload(staffListDto.getFile());

            //파일처리 결과
            if(filename != null){
                //파일 테임이 null 이 아니라면 엔티티의 sfile 에 filename 을 저장한다
                // 실제 파일 저장공간은 c드라이브, 엔티티에는 이름만 저장한다
                entity.setSfile(filename);

            }

            return true;}

        return false;
    }

    // 인사 정보 호출
    @Transactional
    public PageDto staffGet(int page , String keyword){

        // Pageable 인터페이스 // PageRequest 페이지 구현체 .of( 페이지 수 , 한 페이지에 표시할 인사정보 수)
        Pageable pageable = PageRequest.of(page-1 , 3);


        // 커스텀 entityRepository 사용 (keyword -> 검색할 이름 )
        Page<StaffListEntity> entities = staffListEntityRepository.findBySearch(keyword, pageable);

        // dto 에 담을 객체 생성
        List<StaffListDto> staffListDtoList = new ArrayList<>();


        // forEach 활용하여 전체 데이터 dto 타입으로 add
        entities.forEach(staffEntity ->{
            StaffListDto dto = StaffListDto.builder()
                    .sno(staffEntity.getSno())                  // 사번
                    .sname(staffEntity.getSname())              // 이름
                    .sposition(staffEntity.getSposition())      // 직급
                    .sidnum1(staffEntity.getSidnum1())          // 주민번호 앞자리
                    .ssex(staffEntity.getSsex())                // 성별
                    .ssorl(staffEntity.getSsorl())              // 양력, 음력
                    .smarry(staffEntity.getSmarry())            // 결혼정보
                    .sphone(staffEntity.getSphone())            // 연락처
                    .semail(staffEntity.getSemail())            // 이메일
                    .sephone(staffEntity.getSephone())          // 비상연락처
                    .sjoined(staffEntity.getSjoined())          // 입사일
                    .saddress(staffEntity.getSaddress())        // 주소
                    .sdaddress(staffEntity.getSdaddress())      // 상세 주소
                    .build();
            staffListDtoList.add(dto);

            //System.out.println("주민번호 뒷자리 복호화 됐나 ?? : " +  staffListDtoList.get(Integer.parseInt(dto.getSidnum2())) );

        });


        // ------- 페이징 처리 -------//

        // 총 페이지 수
        int totalPage = entities.getTotalPages();
        System.out.println("토탈페이지 : " + totalPage);

        // 총 게시물 수
        long totalCount = entities.getTotalElements();

        PageDto pageDto = PageDto.builder()
                .staffListDtos(staffListDtoList)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .build();
        System.out.println("서비스 pageDto : "+pageDto);
        return pageDto;

    }

    // 인사 정보 수정
    @Transactional
    public boolean staffPut( StaffListDto staffListDto){
        //   staffListDto 에서 sno 받아서 정보 조회
        Optional<StaffListEntity> optional = staffListEntityRepository.findById(staffListDto.getSno());



        // 만약에 isPresent 에 값이 있다면
        if(optional.isPresent()){
            // update 에 정보 대입
            StaffListEntity update = optional.get();

            update.setSposition(staffListDto.getSposition());       // 직급 수정
            update.setSphone(staffListDto.getSphone());             // 핸드폰번호 수정
            update.setSmarry(staffListDto.getSmarry());             // 결혼 수정
            update.setSemail(staffListDto.getSemail());             // 이메일 수정
            update.setSephone(staffListDto.getSephone());           // 비상 연락처 수정


            // js에서 주소값을 value가 아니라 placeholder 로 호출했기에 여기서 공란 여부 확인
            // js에서 가져온 정보가 공란이라면 업데이트 진행 안함 // 공란이 아니라면 업데이트 진행
            // 메인주소
            if(!staffListDto.getSaddress().equals("")){
                //주소가 null이 아니라면 수정
                update.setSaddress(staffListDto.getSaddress());     // 주소 수정
            }
            // 상세주소
            if(!staffListDto.getSdaddress().equals("")){
                // 주소가 null이 아니라면 수정
                update.setSdaddress(staffListDto.getSdaddress());    // 주소 수정
            }

            return true;

        }

        return false;
    }

    // 인사 정보 삭제
    @Transactional
    public boolean staffDelete(int sno) {

        Optional<StaffListEntity> optional = staffListEntityRepository.findById(sno);
        // optional.isPresent() -> 데이터가 있다면 true
        if(optional.isPresent()){
            // 해당 sno 삭제
            staffListEntityRepository.deleteById(sno);
            return true;
        }

        return false;
    }

    // sno로 개별 정보 출력

    @Transactional
    public StaffListDto staffInfoGet(int sno){
        // sno 활용하여 optional 로 감싸서 호출
        Optional<StaffListEntity> optional = staffListEntityRepository.findById(sno);
        // optional.isPresent() -> 데이터가 있다면 true
        if(optional.isPresent()){
            // 가져온 정보를 entity로 대입
            StaffListEntity staffListEntity = optional.get();
            // dto로 타입 변환 후 리턴
            StaffListDto staffListDto = staffListEntity.toDto();

            System.out.println("★★★★★★★개별정보 출력 서비스  staffListDto: " +staffListDto);
            return staffListDto;
        }
        return null;
    }

    // 이름으로 인사 정보 호출
    @Transactional
    public StaffListDto staffNameGet(String sname){

        Optional<StaffListEntity> optional = staffListEntityRepository.findBySname(sname);

        if(optional.isPresent()){
            StaffListEntity staffListEntity = optional.get();
            StaffListDto staffListDto = staffListEntity.toDto();

            System.out.println("★★★★★★★이름으로 검색 staffListDto: " +staffListDto);
            return staffListDto;
        }

        return null;
    }

    //이미지 수정
    @Transactional
    public boolean staffImgUpdate(StaffListDto staffListDto){
        // sno 로 개별 정보 호출
        System.out.println("이미지수정 sno 확인" + staffListDto.getSno());
        System.out.println("이미지수정 sno 확인" + staffListDto.getFile());

        Optional<StaffListEntity> optional = staffListEntityRepository.findById(staffListDto.getSno());
        // 정보가 들어 있다면
       if(optional.isPresent()){
            // file 서비스 통해서 수정 진행
            String fileName = fileService.fileUpload(staffListDto.getFile());
            System.out.println("★★★★★★★★★★★★★★★filename 수정 위해서 확인 " + fileName);

            if(fileName !=null){
                StaffListEntity update= optional.get();
                update.setSfile(fileName);
                return true;
            }
        }

        return false;
    }


}
