package za.co.example.core.impl;

import com.example.products_service.models.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.example.exceptions.ProductNotFoundException;
import za.co.example.exceptions.ProductsNotFoundException;
import za.co.example.persistance.entities.Product;
import za.co.example.persistance.repositories.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductsServiceImpl productsService;

    private Product product;
    private String id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID().toString();
        product = new Product();
        product.setId(id);
        product.setProductName("Widget");
        product.setProductNumber("W-001");
        product.setQuantity(50);
    }

    @Test
    void addProduct_withProductNumber_savesProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO dto = new ProductDTO().productName("Widget").productNumber("W-001").quantity(50);
        productsService.addProduct(dto);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_withNullProductNumber_throwsProductNotFoundException() {
        ProductDTO dto = new ProductDTO().productName("Widget");

        assertThatThrownBy(() -> productsService.addProduct(dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product Number not found");
    }

    @Test
    void removeProduct_withExistingId_deletesProduct() {
        when(productRepository.existsById(id)).thenReturn(true);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productsService.removeProduct(UUID.fromString(id));

        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void removeProduct_withNonExistingId_throwsProductNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(productRepository.existsById(randomId.toString())).thenReturn(false);

        assertThatThrownBy(() -> productsService.removeProduct(randomId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void getAllProducts_withProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = productsService.getAllProducts();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllProducts_withNoProducts_throwsProductsNotFoundException() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productsService.getAllProducts())
                .isInstanceOf(ProductsNotFoundException.class)
                .hasMessageContaining("No product was found");
    }

    @Test
    void getProductById_withExistingId_returnsProduct() {
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDTO result = productsService.getProductById(UUID.fromString(id));

        assertThat(result).isNotNull();
    }

    @Test
    void getProductById_withNonExistingId_throwsProductNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(productRepository.findById(randomId.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productsService.getProductById(randomId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void getProductByName_withExistingName_returnsProduct() {
        when(productRepository.findByProductName("Widget")).thenReturn(product);

        ProductDTO result = productsService.getProductByName("Widget");

        assertThat(result.getProductName()).isEqualTo("Widget");
    }

    @Test
    void getProductByName_withUnknownName_throwsProductsNotFoundException() {
        when(productRepository.findByProductName("Unknown")).thenReturn(null);

        assertThatThrownBy(() -> productsService.getProductByName("Unknown"))
                .isInstanceOf(ProductsNotFoundException.class);
    }

    @Test
    void getProductsByQuantity_withResults_returnsList() {
        when(productRepository.findByQuantity(50)).thenReturn(List.of(product));

        List<ProductDTO> result = productsService.getProductsByQuantity(50);

        assertThat(result).hasSize(1);
    }

    @Test
    void getProductsByQuantity_withNoResults_throwsProductsNotFoundException() {
        when(productRepository.findByQuantity(999)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productsService.getProductsByQuantity(999))
                .isInstanceOf(ProductsNotFoundException.class)
                .hasMessageContaining("Quantity");
    }

    @Test
    void updateProduct_withValidData_updatesAndSaves() {
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO update = new ProductDTO().productName("Updated Widget").quantity(25);
        productsService.updateProduct(UUID.fromString(id), update);

        verify(productRepository, times(1)).save(any(Product.class));
    }
}
