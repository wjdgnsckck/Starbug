package starbug.model.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import starbug.model.entity.product.ProductEntity;

import java.util.List;

@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Integer> {

    @Query(value = "select pro from ProductEntity pro where pro.productCategoryEntity.pcno = :pcno")
    public List<ProductEntity> findCategoryByProduct(@Param(value = "pcno") int pcno);

    public ProductEntity findByPname(@Param(value = "pname")String pname);

    @Query(value = "select * from product where pname like %:keyword% ", nativeQuery = true)
    public List<ProductEntity> findBySearchAll(@Param(value = "keyword")String keyword);

    @Query(value = "select pro from ProductEntity pro where pro.pevent > 0 ")
    public List<ProductEntity> findByEventProduct();

/*    @Query(value = "select * from product where IF(:pcno=0, pname like %:keyword%, " +
            "IF(:pcno=-1, pevent>0, pcno = :pcno))", nativeQuery = true)*/
/*@Query(value = "SELECT * FROM product WHERE " +
        "CASE " +
        "   WHEN :pcno = 0 THEN pname LIKE %:keyword% " +
        "   WHEN :pcno = -1 THEN pevent > 0 " +
        "   ELSE pcno = :pcno " +
        "END order by :searchselect", nativeQuery = true)*/
@Query(value = "SELECT * FROM product WHERE " +
        "(:pcno = 0 AND pname LIKE %:keyword%) OR " +
        "(:pcno = -1 AND pevent > 0) OR " +
        "(:pcno > 0 AND pcno = :pcno)", nativeQuery = true)
    public List<ProductEntity> findByProducts(@Param(value = "pcno")int pcno, @Param(value = "keyword")String keyword);
}
