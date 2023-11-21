package starbug.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.staff.SalaryDto;
import starbug.model.entity.staff.SalaryEntity;
import starbug.model.repository.staff.SalaryEntityRepository;
import starbug.model.repository.staff.StaffListEntityRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    private SalaryEntityRepository salaryEntityRepository;
    @Autowired
    private StaffListEntityRepository staffListEntityRepository;

    // 급여 등록
    @Transactional
    public boolean salaryPost(SalaryDto salaryDto){
        // salary entity 담기위해 호출
        SalaryEntity salaryEntity = salaryDto.saveEntity();
        // sno 호출해서 salrary entity 에 추가
        salaryEntity.setStaffListEntity(staffListEntityRepository.findById(salaryDto.getSno()).get());
        // 가져온 정보 save
        SalaryEntity result = salaryEntityRepository.save(salaryEntity);

        // 등록 후 반환엔 entity에서 slno(급여번호 pk) 가 1이상 이라면 정상등록 // true 반환
        if(salaryEntity.getSlno() >= 1){return true;}
        return false;
    }

    // 급여 정보 호출
    @Transactional
    public PageDto salaryGet(int page ,int sno){

        // Pageable 인터페이스 // PageRequest 페이지 구현체 .of( 페이지 수 , 한 페이지에 표시할 인사정보 수)
        Pageable pageable = PageRequest.of(page-1 , 5);


        // 커스텀 -> sno(회원번호) 와 페이지
        Page<SalaryEntity> entities = salaryEntityRepository.findSalaryGet(sno , pageable);


        // list 로 객체 저장 준비
        List<SalaryDto> salaryDtosList =new ArrayList<>();
        // dto  타입으로 add
        entities.forEach(salaryEntity -> {
            SalaryDto salaryDto = SalaryDto.builder()
                    .slno(salaryEntity.getSlno())
                    .sbasepay(salaryEntity.getSbasepay())
                    .sincentive(salaryEntity.getSincentive())
                    .sdeductible(salaryEntity.getSdeductible())
                    .sdate(salaryEntity.getSdate())
                    .build();
            salaryDtosList.add(salaryDto);
        });




        // 페이징처리

        // 총 페이지 수
        int totalPage = entities.getTotalPages();


        // 총 게시물 수
        long totalCount = entities.getTotalElements();

        PageDto pageDto = PageDto.builder()
                .salaryDtos(salaryDtosList)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .build();


        return pageDto;
    }

    // 급여 정보 수정
    @Transactional
    public boolean salaryPut(SalaryDto salaryDto){
        System.out.println("★★★★★★★★★ 급여수정 salaryDto : " + salaryDto);
        // salaryDto에 있는 slno 활용하여 정보 호출
        Optional<SalaryEntity> optional = salaryEntityRepository.findById(salaryDto.getSlno());

        // 호출된 정보가 있다면
        if(optional.isPresent()){
            // entity 로 업데이트 위해 가져온 정보 저장
            SalaryEntity update = optional.get();

            System.out.println(salaryDto.getSbasepay());
            System.out.println(salaryDto.getSincentive());
            System.out.println(salaryDto.getCdate());

            update.setSbasepay(salaryDto.getSbasepay());
            update.setSincentive(salaryDto.getSincentive());
            update.setSdeductible(salaryDto.getSdeductible());
            update.setSdate(salaryDto.getSdate());

            return true;
        }


        return false;
    }

    // 급여 정보 삭제
    @Transactional
    public boolean salaryDelete(int slno){
        // slno 활용하여 데이터 호출
        Optional<SalaryEntity> optional = salaryEntityRepository.findById(slno);

        // 가져온 데이터가 있다면
        if(optional.isPresent()) {
            // 해당 snlo 삭제
            salaryEntityRepository.deleteById(slno);
            return true;
        }
        return false;
    }

    // 급여 개별 정보 호출
    @Transactional
    public SalaryDto salaryGetInfo(int slno){
        // snlo 활용하여 급여 정보 호출
        Optional<SalaryEntity> optional = salaryEntityRepository.findById(slno);

        // 만약에 otional 에 데이터가 있다면
        if(optional.isPresent()){

            // 가져온 데이터를 entity로 대입
            SalaryEntity salaryEntity = optional.get();

            // dto타읍으로 변환 후 리턴
            SalaryDto salaryDto = salaryEntity.toDto();


           return salaryDto;

        }

        return null;
    }

}
