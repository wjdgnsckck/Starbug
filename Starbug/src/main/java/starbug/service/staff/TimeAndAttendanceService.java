package starbug.service.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.staff.TimeAndAttendanceDto;
import starbug.model.entity.staff.TimeAndAttendanceEntity;
import starbug.model.repository.staff.SalaryEntityRepository;
import starbug.model.repository.staff.StaffListEntityRepository;
import starbug.model.repository.staff.TimeAndAttendanceEntityRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeAndAttendanceService {

    @Autowired
    private SalaryEntityRepository salaryEntityRepository;
    @Autowired
    private StaffListEntityRepository staffListEntityRepository;

    @Autowired
    private TimeAndAttendanceEntityRepository taaRepository;

    // 근태 등록
    @Transactional
    public boolean taaPost(TimeAndAttendanceDto timeAndAttendanceDto){
        // salary entity 담기위해 호출
        TimeAndAttendanceEntity timeAndAttendanceEntity = timeAndAttendanceDto.saveEntity();
        // sno 호출해서 salrary entity 에 추가
        timeAndAttendanceEntity.setStaffListEntity(staffListEntityRepository.findById(timeAndAttendanceDto.getSno()).get());
        // 가져온 정보 save
        TimeAndAttendanceEntity result = taaRepository.save(timeAndAttendanceEntity);

        // tno pk 번호가 1 이상이라면 등록 정상 // true 반환
        if(timeAndAttendanceEntity.getTno() >=1){
            return true;
        }
        return false;
    }

    // 근태 정보 호출
    @Transactional
    public PageDto taaGet(int page, int sno){
        // Pageable 인터페이스 // PageRequest 페이지 구현체 .of( 페이지 수 , 한 페이지에 표시할 인사정보 수)
        Pageable pageable = PageRequest.of(page-1 , 5);

        Page<TimeAndAttendanceEntity> entities = taaRepository.findTaaGet(sno, pageable);

        System.out.println("★★★★★★★★★★★★★★근태 호출 서비스 entitis 가져왔는지"+ entities);

        List<TimeAndAttendanceDto> taaDtoList = new ArrayList<>();

        entities.forEach( taaEntity ->{
            TimeAndAttendanceDto dto = TimeAndAttendanceDto.builder()
                    .tno(taaEntity.getTno())
                    .tstate(taaEntity.getTstate())
                    .tdate(taaEntity.getTdate())
                    .build();
            taaDtoList.add(dto);
        } );


        // 페이징처리

        // 총 페이지 수
        int totalPage = entities.getTotalPages();


        // 총 게시물 수
        long totalCount = entities.getTotalElements();

        PageDto pageDto = PageDto.builder()
                .timeAndAttendanceDtos(taaDtoList)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .build();

        return pageDto;
    }

    // 근태 수정
    @Transactional
    public boolean taaPut(TimeAndAttendanceDto dto){

        System.out.println("근태수정 tno " + dto.getTno());
        Optional<TimeAndAttendanceEntity> optional = taaRepository.findById(dto.getTno());

        if(optional.isPresent()){

            TimeAndAttendanceEntity update = optional.get();
            update.setTstate(dto.getTstate());
            update.setTdate(dto.getTdate());

            return true;
        }

        return false;
    }

    // 근태 삭제
    @Transactional
    public boolean taaDelete(int tno){

        Optional<TimeAndAttendanceEntity> optional = taaRepository.findById(tno);

        if(optional.isPresent()){
            taaRepository.deleteById(tno);
            return true;
        }

        return false;
    }

    // 근태 정보 개별 호출
    @Transactional
    public TimeAndAttendanceDto taaGetInfo(int tno){

        Optional<TimeAndAttendanceEntity> optional = taaRepository.findById(tno);

        if(optional.isPresent()){
            TimeAndAttendanceEntity entity = optional.get();
            TimeAndAttendanceDto dto = entity.toDto();
            return dto;
        }


        return null;
    }

}
