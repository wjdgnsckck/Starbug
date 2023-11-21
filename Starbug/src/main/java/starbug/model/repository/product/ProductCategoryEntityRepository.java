package starbug.model.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.product.ProductCategoryEntity;

import java.util.List;

@Repository
public interface ProductCategoryEntityRepository extends JpaRepository<ProductCategoryEntity, Integer> {

    @Query(value ="select * from product_category order by pcno", nativeQuery = true)
    public List<ProductCategoryEntity> findAllCategory();
}
