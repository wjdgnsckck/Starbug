package starbug.service.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import starbug.model.dto.page.PageDto;
import starbug.model.dto.resoures.ResouresDto;
import starbug.model.dto.staff.SalaryDto;
import starbug.model.entity.parcel.ParcellogEntity;
import starbug.model.entity.staff.SalaryEntity;
import starbug.model.repository.resoures.ParcellogEntityRepository;
import starbug.model.repository.resoures.ResouresEntityRepository;
import starbug.model.repository.staff.SalaryEntityRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class PurchaseService {

    @Autowired // 급여
    private SalaryEntityRepository salaryEntityRepository;
    @Autowired // 발주로그
    private ParcellogEntityRepository parcellogEntityRepository;
    @Autowired // 재료 ( 사용할지 안할지 모르겠음 )
    private ResouresEntityRepository resouresEntityRepository;


    // 1. 총 발주 내역 출력하기 ============================================================================================
    public PageDto getPurchaseList(int page, String key, String keyword){

        Pageable pageable  = PageRequest.of(page-1, 10 );
        // 재료테이블과 조인해서 발주내역 가져오기
        Page<ParcellogEntity> parcellogEntities = parcellogEntityRepository.findByPurchaseListAll(key, keyword, pageable);
        // 변환하기
        List<ResouresDto> parcellogDtos = new ArrayList<>();
        parcellogEntities.forEach( (p) -> {
            parcellogDtos.add(p.toParcellogDto());
        });
        // 총 페이지수
        int totalPages = parcellogEntities.getTotalPages();
        // 총 게시물수
        long totalCount = parcellogEntities.getTotalElements();

        // DTO 구성해서 반환하기
        PageDto resuldto = PageDto.builder()
                .parcellogDtos(parcellogDtos)
                .totalPage(totalPages)
                .totalCount( totalCount )
                .build();
        System.out.println("서비스에서 발주 리스트 반환 :"+resuldto);
        return resuldto;
    }


    // 2. 총 급여 내역 출력하기 ============================================================================================
    public PageDto getSalaryList(int page, String key, String keyword){
        System.out.println("급여 출력 서비스도 입장 했나????????");
        Pageable pageable  = PageRequest.of(page-1, 10 );

        // 급여 내역 불러오기
        Page<SalaryEntity> SalaryEntites = salaryEntityRepository.findSalaryAll(key, keyword, pageable);


        // 변환하기
        List<SalaryDto> salaryDtos = new ArrayList<>();
        SalaryEntites.forEach( (s) -> {
            salaryDtos.add(s.toDto());
        });

        // 총 페이지수
        int totalPages = SalaryEntites.getTotalPages();
        // 총 게시물수
        long totalCount = SalaryEntites.getTotalElements();

        // 총 지급액 계산
        // sbasepay + sincentive - sdeductible
        // 기본급 + 추가금 - 차감금액
        salaryDtos.forEach( (s) -> {
            int sbasepay = s.getSbasepay() + s.getSincentive() -s.getSdeductible();
            s.setSbasepay(sbasepay);
        });

        // DTO 구성해서 반환하기
        PageDto resuldto = PageDto.builder()
                .salaryDtos(salaryDtos)
                .totalPage(totalPages)
                .totalCount( totalCount )
                .build();
        System.out.println("서비스에서 급여 리스트 반환 :"+resuldto);
        return resuldto;
    }


}
