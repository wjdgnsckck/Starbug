package starbug.model.repository.resoures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import starbug.model.entity.resoures.RecipeEntity;

import java.util.List;


public interface RecipeEntityRepository extends JpaRepository<RecipeEntity, Integer> {

    @Query(value = "select recipe from RecipeEntity recipe where recipe.productEntity.pno = :pno")
    public List<RecipeEntity> findByPno(@Param(value = "pno") int pno); // pno를 이용하여 레시피 찾기

}
