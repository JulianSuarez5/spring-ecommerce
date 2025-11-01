package ppi.e_commerce.Repository;

import ppi.e_commerce.Model.OrderDetail;
import ppi.e_commerce.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    
    List<OrderDetail> findByOrder(Order order);
    
    List<OrderDetail> findByOrderOrderById(Order order);
}
