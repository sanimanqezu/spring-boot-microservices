package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.threeten.bp.LocalDate;
import za.co.example.persistance.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByProductName(String productName);

    List<Product> findByProductNumber(String productNumber);

    List<Product> findByExpirationDate(LocalDate expirationDate);

    List<Product> findByQuantity(Integer quantity);
}
