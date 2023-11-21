package starbug.model.repository.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starbug.model.entity.member.CouponEntity;
import starbug.model.entity.member.CouponHistoryEntity;
import starbug.model.entity.member.StarBugMemberEntity;

import java.util.List;

@Repository
public interface CouponEntityRepository extends JpaRepository<CouponEntity , Integer> {




}
