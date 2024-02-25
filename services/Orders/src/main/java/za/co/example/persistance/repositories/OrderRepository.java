package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.example.persistance.entities.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByOrderNumber(String orderNumber);

    List<Order> findByQuantity(Integer quantity);

    List<Order> findByProduct(String product);

}
