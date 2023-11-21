package starbug.model.repository.resoures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starbug.model.entity.resoures.RescategoryEntity;

@Repository
public interface RescategoryEntityRepository
        extends JpaRepository<RescategoryEntity, Integer> {
    RescategoryEntity findByRescname(String rescname);
}
