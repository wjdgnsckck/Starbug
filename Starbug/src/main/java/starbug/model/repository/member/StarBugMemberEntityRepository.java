package starbug.model.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.member.StarBugMemberEntity;

@Repository
public interface StarBugMemberEntityRepository extends JpaRepository<StarBugMemberEntity , Integer> {

    @Query
            (value="select * from members where "+
                    " if( :keyword = '' , true , "+
                    " if( :key = 'mname' , mname like %:keyword% , "+
                    " if( :key = 'mphone' , mphone like %:keyword% , true ))) and mname !='탈퇴한 회원'"
                    , nativeQuery = true)
    Page<StarBugMemberEntity> findByMserch(String key , String keyword , Pageable pageable);
}
