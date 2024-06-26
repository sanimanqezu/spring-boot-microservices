package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import za.co.example.persistance.entities.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByOrderNumber(String orderNumber);

    List<Order> findByQuantity(Integer quantity);

    Order findByOrdererIdNo(String ordererIdNo);
}
