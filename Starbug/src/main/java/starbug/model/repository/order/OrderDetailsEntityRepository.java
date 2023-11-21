package starbug.model.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import starbug.model.entity.order.OrderDetailsEntity;
import starbug.model.entity.order.OrdersEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDetailsEntityRepository
        extends JpaRepository<OrderDetailsEntity,Integer> {

    @Query( value = "select  o.ono, o.odate, o.ostate, o.paid, o.totalprice, o.mno, " +
            " od.odtno, od.pno, p.pname,  m.mname from orders o " +
            " natural join orders_details od " +
            "    natural join product p " +
            "    natural join members m" +
            "    where o.ono = :ono", nativeQuery = true)
    List<Map< Integer, Object >> findByOno(int ono);


}
