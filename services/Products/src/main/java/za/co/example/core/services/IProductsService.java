package za.co.example.core.services;

import com.example.products_service.models.ProductDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IProductsService {

    void addProduct(ProductDTO productDTO);

    void removeProduct(UUID id);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(UUID id);

    ProductDTO getProductByName(String productName);

    ProductDTO getProductByNumber(String productNumber);

    List<ProductDTO> getProductsByExpirationDate(LocalDateTime expirationDate);

    void updateProduct(UUID id, ProductDTO updatedProduct);

    List<ProductDTO> searchProducts(UUID id, String productName, String productNumber, Integer quantity, LocalDateTime expirationDate);

    List<ProductDTO> getProductsByQuantity(Integer quantity);
}
