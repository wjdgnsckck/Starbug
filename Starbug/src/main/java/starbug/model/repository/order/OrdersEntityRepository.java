package starbug.model.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.order.OrdersEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface OrdersEntityRepository
        extends JpaRepository<OrdersEntity,Integer> {

    // 주문내역 출력하기
    @Query( value = " select * from orders where " +
            " if( :keyword = '', true, " +                              // 전체 검색
            " if( :key = 'mno', mno = :keyword , " +                    // 주문자 검색
            " if( :key = 'ostate', ostate like %:keyword% , " +         // 주문상태 검색
            " if( :key = 'odate', odate like %:keyword% , true )))) " + // 주문날짜 검색
            " order by odate desc"
            , nativeQuery = true)
    Page<OrdersEntity> findBySearch(String key, String keyword, Pageable pageable);

    // 차트에서 상품별 출력
    @Query(value = "SELECT  " +
            "    p.pcno, pc.pcname, " +
            "    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) " +
            "    OVER (PARTITION BY p.pcno), 0) AS percentage " +
            " FROM  " +
            "    orders o " +
            "    NATURAL JOIN orders_details od " +
            "    NATURAL JOIN product p " +
            "    NATURAL JOIN product_category pc " +
            " WHERE  " +
            "    o.ostate = 0 " +
            "    AND o.odate BETWEEN :startDate AND :endDate " +
            " GROUP BY  " +
            "    p.pcno, pc.pcname " +
            " ORDER BY  " +
            "    p.pcno, pc.pcname",
            nativeQuery = true)
    List<Map< String, Object >> findBySearchChartForProduct(String startDate, String endDate);

    // 차트에 매출 값
    @Query(value = "SELECT " +
            "    months.month, " +
            "    COALESCE(SUM(CASE WHEN ostate = 0 THEN totalprice ELSE 0 END), 0) AS sales " +
            "FROM " +
            "    (SELECT CONCAT(:year, '-01') AS month " +
            "    UNION SELECT CONCAT(:year, '-02') " +
            "    UNION SELECT CONCAT(:year, '-03') " +
            "    UNION SELECT CONCAT(:year, '-04') " +
            "    UNION SELECT CONCAT(:year, '-05') " +
            "    UNION SELECT CONCAT(:year, '-06') " +
            "    UNION SELECT CONCAT(:year, '-07') " +
            "    UNION SELECT CONCAT(:year, '-08') " +
            "    UNION SELECT CONCAT(:year, '-09') " +
            "    UNION SELECT CONCAT(:year, '-10') " +
            "    UNION SELECT CONCAT(:year, '-11') " +
            "    UNION SELECT CONCAT(:year, '-12')) AS months " +
            "LEFT JOIN " +
            "    orders ON DATE_FORMAT(odate, '%Y-%m') = months.month " +
            "          AND YEAR(odate) = :year " +
            "GROUP BY " +
            "    months.month " +
            "ORDER BY " +
            "    months.month; "
            , nativeQuery = true)
    List<Map< String, Object >> findBySearchChartForSales(String year );


    // 차트에 매입값
    @Query(value = "SELECT " +
            "    months.month, " +
            "    COALESCE(SUM(total_purchase), 0) AS purchase " +
            "FROM " +
            "    (SELECT " +
            "        months.month, " +
            "        COALESCE(SUM(pl.pcount * r.resprice), 0) AS total_purchase " +
            "    FROM " +
            "        (SELECT CONCAT(:year, '-01') AS month " +
            "         UNION SELECT CONCAT(:year, '-02') " +
            "         UNION SELECT CONCAT(:year, '-03') " +
            "         UNION SELECT CONCAT(:year, '-04') " +
            "         UNION SELECT CONCAT(:year, '-05') " +
            "         UNION SELECT CONCAT(:year, '-06') " +
            "         UNION SELECT CONCAT(:year, '-07') " +
            "         UNION SELECT CONCAT(:year, '-08') " +
            "         UNION SELECT CONCAT(:year, '-09') " +
            "         UNION SELECT CONCAT(:year, '-10') " +
            "         UNION SELECT CONCAT(:year, '-11') " +
            "         UNION SELECT CONCAT(:year, '-12')) AS months " +
            "    LEFT JOIN " +
            "        parcel_log pl ON DATE_FORMAT(pl.cdate, '%Y-%m') = months.month " +
            "    LEFT JOIN " +
            "        resources r ON pl.resno = r.resno " +
            "    GROUP BY " +
            "        months.month " +
            "    UNION " +
            "    SELECT " +
            "        months.month, " +
            "        COALESCE(SUM(s.sbasepay + s.sincentive - s.sdeductible), 0) AS total_purchase " +
            "    FROM " +
            "        (SELECT CONCAT(:year, '-01') AS month " +
            "         UNION SELECT CONCAT(:year, '-02') " +
            "         UNION SELECT CONCAT(:year, '-03') " +
            "         UNION SELECT CONCAT(:year, '-04') " +
            "         UNION SELECT CONCAT(:year, '-05') " +
            "         UNION SELECT CONCAT(:year, '-06') " +
            "         UNION SELECT CONCAT(:year, '-07') " +
            "         UNION SELECT CONCAT(:year, '-08') " +
            "         UNION SELECT CONCAT(:year, '-09') " +
            "         UNION SELECT CONCAT(:year, '-10') " +
            "         UNION SELECT CONCAT(:year, '-11') " +
            "         UNION SELECT CONCAT(:year, '-12')) AS months " +
            "    LEFT JOIN " +
            "        salary s ON DATE_FORMAT(s.sdate, '%Y-%m') = months.month " +
            "    GROUP BY " +
            "        months.month) AS months " +
            "GROUP BY " +
            "    months.month " +
            "ORDER BY " +
            "    months.month"
            , nativeQuery = true)
    List<Map< String, Object >> findBySearchChartForBuy(String year );


    // 순이익에서 매출총액과 총실결제금액 구하기

    @Query(value = "SELECT " +
            "    SUM(CASE WHEN ostate = 0 THEN paid ELSE 0 END) AS total_paid, " +
            "    SUM(CASE WHEN ostate = 0 THEN totalprice ELSE 0 END) AS total_totalprice " +
            "FROM " +
            "    orders " +
            "WHERE " +
            "    odate BETWEEN :startDate AND :endDate"
            , nativeQuery = true)
    Map<String, Integer> findByIncomeSales(String startDate, String endDate);



}
