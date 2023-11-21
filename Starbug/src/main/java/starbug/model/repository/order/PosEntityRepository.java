package starbug.model.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import starbug.model.entity.product.ProductEntity;

public interface PosEntityRepository
        extends JpaRepository<ProductEntity,Integer> {
}
