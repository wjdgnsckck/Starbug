package starbug.model.repository.staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.staff.TimeAndAttendanceEntity;

@Repository
public interface TimeAndAttendanceEntityRepository extends JpaRepository<TimeAndAttendanceEntity,Integer> {

    @Query(value="select * from time_and_attendance where sno_fk = :sno order by tdate" , nativeQuery = true)
    Page<TimeAndAttendanceEntity> findTaaGet(int sno , Pageable pageable);

}
