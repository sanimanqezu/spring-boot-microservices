package za.co.example.core.services;

import com.example.products_service.models.ProductDTO;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.UUID;

public interface IProductsService {

    void addProduct(ProductDTO productDTO);

    void removeProduct(UUID id);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(UUID id);

    List<ProductDTO> getProductsByName(String productName);

    List<ProductDTO> getProductsByNumber(String productNumber);

    List<ProductDTO> getProductsByExpirationDate(LocalDate expirationDate);


    void updateProduct(UUID id, ProductDTO updatedProduct);

    List<ProductDTO> searchProducts(UUID id, String productName, String productNumber, Integer quantity, LocalDate expirationDate);

    List<ProductDTO> getProductsByQuantity(Integer quantity);
}
