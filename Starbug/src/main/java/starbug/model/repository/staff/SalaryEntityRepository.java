package starbug.model.repository.staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.staff.SalaryEntity;

import java.util.Map;

@Repository
public interface SalaryEntityRepository extends JpaRepository<SalaryEntity , Integer> {
    // 예지 작성 : 매입 탭 급여지급 내역 출력 -----------------------------------------------------------
    @Query(value = "select * " +
            " from salary natural join staff_list where " +
            " if( :keyword = '', true, " +                            // 전체 검색
            " if( :key = 'sname', sname like %:keyword% , " +        // 사원명 검색
            " if( :key = 'sdate', sdate like %:keyword% , true )))" +
            " order by sdate desc " // 지급날짜 검색
            , nativeQuery = true )
    Page<SalaryEntity> findSalaryAll(String key, String keyword, Pageable pageable);

    // 예지 작성 : 손익계산서 -----------------------------------------------------------
    @Query(value = "select " +
            "    SUM(sbasepay + sincentive - sdeductible) AS totalSalary " +
            " from " +
            "    salary " +
            " where " +
            "    sdate BETWEEN :startDate AND :endDate"
            , nativeQuery = true )
    Integer  findByIncomeSalary(String startDate, String endDate );



    @Query(value="select * from salary where sno = :sno order by sdate" , nativeQuery = true)
    Page<SalaryEntity> findSalaryGet(int sno , Pageable pageable);

}
