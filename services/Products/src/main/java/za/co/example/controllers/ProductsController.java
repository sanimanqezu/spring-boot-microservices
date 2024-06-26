package za.co.example.controllers;

import com.example.products_service.api.ProductsApi;
import com.example.products_service.models.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import za.co.example.core.services.IProductsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
public class ProductsController implements ProductsApi{

    private final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    private final IProductsService productsService;


    @Override
    public ResponseEntity<Void> addProduct(ProductDTO body) {
        logger.info("Adding product: {}", body.toString());
        productsService.addProduct(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        logger.info("Retrieving all products");
        return ResponseEntity.ok(productsService.getAllProducts());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductByExpirationDate(LocalDateTime expirationDate) {
        logger.info("Retrieving product(s) by expiration date {}", expirationDate);
        return ResponseEntity.ok(productsService.getProductsByExpirationDate(expirationDate));
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(UUID id) {
        logger.info("Retrieving a product by id: {}", id);
        return ResponseEntity.ok(productsService.getProductById(id));
    }

    @Override
    public ResponseEntity<ProductDTO> getProductByName(String productName) {
        logger.info("Retrieving product(s) by product name: {}", productName);
        return ResponseEntity.ok(productsService.getProductByName(productName));
    }

    @Override
    public ResponseEntity<ProductDTO> getProductByNumber(String productNumber) {
        logger.info("Retrieving product(s) by product number: {}", productNumber);
        return ResponseEntity.ok(productsService.getProductByNumber(productNumber));
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductsByQuantity(Integer quantity) {
        logger.info("Retrieving product(s) by product quantity: {}", quantity);
        return ResponseEntity.ok(productsService.getProductsByQuantity(quantity));
    }

    @Override
    public ResponseEntity<Void> removeProductById(UUID id) {
        logger.info("Removing a product by Id: {}", id);
        productsService.removeProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> updateProductById(UUID id, ProductDTO body)  {
        logger.info("Updating product identified by id: {}, with product: {}", id, body);
        productsService.updateProduct(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<ProductDTO>> searchProduct(UUID id, String productName, String productNumber, Integer quantity, LocalDateTime expirationDate) {
        logger.info("Searching product(s) by the following properties: \n Id: {} \n Product Name: {} \n  Product Number: {}\n  Product Quantity: {} \n expiration Date : {} ",
                id, productName, productNumber, quantity, expirationDate);
        return ResponseEntity.ok(productsService.searchProducts(id, productName, productNumber, quantity, expirationDate));
    }
}
