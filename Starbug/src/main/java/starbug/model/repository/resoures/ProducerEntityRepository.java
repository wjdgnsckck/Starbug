package starbug.model.repository.resoures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starbug.model.entity.parcel.ProducerEntity;

@Repository
public interface ProducerEntityRepository
        extends JpaRepository<ProducerEntity, Integer> {
}
