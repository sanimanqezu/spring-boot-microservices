package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.example.persistance.entities.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findByProductName(String productName);

    List<Product> findByProductNumber(String productNumber);

    List<Product> findByExpirationDate(LocalDateTime expirationDate);

    List<Product> findByQuantity(Integer quantity);
}
