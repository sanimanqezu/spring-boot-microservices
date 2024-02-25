package za.co.example.core.impl;

import com.example.products_service.models.ProductDTO;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import za.co.example.core.services.IProductsService;
import za.co.example.exceptions.ProductNotFoundException;
import za.co.example.exceptions.ProductsNotFoundException;
import za.co.example.mappers.ProductMapper;
import za.co.example.persistance.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsServiceImpl implements IProductsService {

    private final ProductRepository productRepository;

    public ProductsServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(ProductDTO productDTO) {
        String productNumber = productDTO.getProductNumber();

        if (productNumber == null) {
            throw new ProductNotFoundException("Product Number not found");
        }
        productRepository.save(ProductMapper.PRODUCT_MAPPER.dtoToEntity(productDTO));
    }

    @Override
    public void removeProduct(UUID id) {
        boolean product = productRepository.existsById(id);

        if (!product) {
            throw new ProductNotFoundException("Id", id);
        }
        productRepository.delete(ProductMapper.PRODUCT_MAPPER.dtoToEntity(getProductById(id)));
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findAll());

        if (products.isEmpty()) {
            throw new ProductsNotFoundException("No product was found!!");
        }
        return products;
    }

    @Override
    public ProductDTO getProductById(UUID id) {
        ProductDTO productOptional = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findById(id).get());

        if (productOptional == null) {
            throw new ProductNotFoundException("Id", id);
        }
        return productOptional;
    }

    @Override
    public List<ProductDTO> getProductsByName(String productName) {
        List<ProductDTO> products = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByProductName(productName));
        if (products == null || products.isEmpty()) {
            throw new ProductsNotFoundException("Product Name", productName);
        }
        return products;
    }

    @Override
    public List<ProductDTO> getProductsByNumber(String productNumber) {
        List<ProductDTO> products = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByProductNumber(productNumber));
        if (products == null || products.isEmpty()) {
            throw new ProductsNotFoundException("Product Number", productNumber);
        }
        return products;
    }

    @Override
    public List<ProductDTO> getProductsByExpirationDate(LocalDate expirationDate) {
        List<ProductDTO> products = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByExpirationDate(expirationDate));
        if (products == null || products.isEmpty()) {
            throw new ProductsNotFoundException("Expiration Date", expirationDate);
        }
        return products;
    }

    @Override
    public void updateProduct(UUID id, ProductDTO updatedProduct) {
        ProductDTO existingProduct = getProductById(id);
        if (existingProduct != null && updatedProduct != null) {
            if (updatedProduct.getProductName() != null && !updatedProduct.getProductName().isEmpty()) existingProduct.setProductName(updatedProduct.getProductName());
            if (updatedProduct.getProductNumber() != null && !updatedProduct.getProductNumber().isEmpty()) existingProduct.setProductNumber(updatedProduct.getProductNumber());
            if (updatedProduct.getQuantity() != null ) existingProduct.setQuantity(updatedProduct.getQuantity());
            if (updatedProduct.getExpirationDate() != null ) existingProduct.setExpirationDate(updatedProduct.getExpirationDate());
            productRepository.save(ProductMapper.PRODUCT_MAPPER.dtoToEntity(existingProduct));
        }
    }

    @Override
    public List<ProductDTO> searchProducts(UUID id, String productName, String productNumber, Integer quantity, LocalDate expirationDate) {
        List<ProductDTO> products = new ArrayList<>();
        if (id != null) {
            products.add(ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findById(id).get()));
        }
        if (productName != null && !productName.isEmpty()) {
            products.addAll(ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByProductName(productName)));
        }
        if (productNumber != null && !productNumber.isEmpty()) {
            products.addAll(ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByProductNumber(productNumber)));
        }
        if (quantity != null) {
            products.addAll(ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByQuantity(quantity)));
        }
        if (expirationDate != null ) {
            products.addAll(ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByExpirationDate(expirationDate)));
        }
        return products;
    }

    @Override
    public List<ProductDTO> getProductsByQuantity(Integer quantity) {
        List<ProductDTO> products = ProductMapper.PRODUCT_MAPPER.entityToDto(productRepository.findByQuantity(quantity));
        if (products == null || products.isEmpty()) {
            throw new ProductsNotFoundException("Quality", quantity);
        }
        return products;
    }
}
