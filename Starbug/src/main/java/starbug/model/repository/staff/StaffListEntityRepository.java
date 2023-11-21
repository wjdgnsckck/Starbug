package starbug.model.repository.staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.staff.StaffListEntity;

import java.util.Optional;

@Repository
public interface StaffListEntityRepository extends JpaRepository<StaffListEntity , Integer> {

    @Query(value="select * from staff_list where " +
            "if(:keyword = '' , true , sname like %:keyword% )"
            , nativeQuery = true)
    Page<StaffListEntity> findBySearch(String keyword ,Pageable pageable);

    // sname 으로 정보 검색 커스텀
    Optional<StaffListEntity> findBySname(String sname);
}
