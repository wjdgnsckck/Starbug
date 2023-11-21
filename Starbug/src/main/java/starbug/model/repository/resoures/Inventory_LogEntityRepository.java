package starbug.model.repository.resoures;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.inventory.InventoryLogEntity;
import starbug.model.entity.resoures.ResouresEntity;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Repository
public interface Inventory_LogEntityRepository
    extends JpaRepository<InventoryLogEntity, Integer> {
    //resno에 맞는 판매개수를 구하기 위한 로직
    @Query ( value = " SELECT sum(log.inloghistory) FROM inventory_log as log WHERE inlogdate >= CONCAT(:date, ' 00:00:00.000000') AND " +
            " inlogdate <= CONCAT(:date, ' 23:59:59.999999')" +
            " AND inloghistory<0 AND resno=:resno",nativeQuery = true)
        Object findBysaleCount(String date , int resno);


    //날짜에 맞는 재고 로그를 출력해주는 쿼리문 이거는 발주용...
    @Query(value = "SELECT * FROM inventory_log as log WHERE inlogdate >= CONCAT(:date, ' 00:00:00.000000') AND " +
            "inlogdate <= CONCAT(:date, ' 23:59:59.999999') group by resno order by inlogno asc", nativeQuery = true)
    //날짜에맞는 데이터를 가져오는 쿼리문(가져올시 resno의 중복값 제거를 위해group by를 사용한다.)
    List<InventoryLogEntity> findByInlogdateAfter(String date);

    //날짜에 맞는 재고 로그를 출력해주는 쿼리문
    @Query(value = "SELECT * FROM inventory_log as log WHERE inlogdate >= CONCAT(:date, ' 00:00:00.000000') AND " +
            "inlogdate <= CONCAT(:date, ' 23:59:59.999999') order by inlogno asc", nativeQuery = true)
    //날짜에맞는 데이터를 가져오는 쿼리문(가져올시 resno의 중복값 제거를 위해group by를 사용한다.)
    List<InventoryLogEntity> findByInlogdate(String date);

    //검색에 맞는 재고 로그 출력하기
    @Query(value = "SELECT * FROM inventory_log il " +
            "LEFT JOIN resources r ON il.resno = r.resno " +
            "WHERE " +
            "IF(:resname = '', true, r.resname LIKE %:resname% ) " +
            "ORDER BY inlogdate DESC", nativeQuery = true)
    Page<InventoryLogEntity> findBySearch(String resname , Pageable pageable );

    //재료개수 구하는 쿼리문
    @Query( value = "select sum(inventory_log.inloghistory) from inventory_log where resno=:resno",nativeQuery = true)
    Double findByCount(int resno);
}
