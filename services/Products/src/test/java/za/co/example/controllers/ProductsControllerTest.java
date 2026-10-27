package za.co.example.controllers;

import com.example.products_service.models.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import za.co.example.core.services.IProductsService;
import za.co.example.exceptions.ProductNotFoundException;
import za.co.example.exceptions.ProductsNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IProductsService productsService;

    @Test
    void addProduct_returns201_andDelegatesToService() throws Exception {
        ProductDTO body = new ProductDTO().productName("Widget").productNumber("W-001").quantity(50);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        verify(productsService, times(1)).addProduct(any(ProductDTO.class));
    }

    @Test
    void getAllProducts_returns200AndBody() throws Exception {
        when(productsService.getAllProducts())
                .thenReturn(List.of(new ProductDTO().productName("Widget").quantity(50)));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Widget"));
    }

    @Test
    void getProductById_returns200_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(productsService.getProductById(id))
                .thenReturn(new ProductDTO().id(id).productName("Widget"));

        mockMvc.perform(get("/products/id").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Widget"));
    }

    @Test
    void getProductById_returns404_whenServiceThrowsProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(productsService.getProductById(id))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/products/id").param("id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

    @Test
    void getAllProducts_returns404_whenServiceThrowsProductsNotFound() throws Exception {
        when(productsService.getAllProducts())
                .thenThrow(new ProductsNotFoundException("No product was found"));

        mockMvc.perform(get("/products"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No product was found"));
    }

    @Test
    void getProductById_returns400_whenIdParamMissing() throws Exception {
        mockMvc.perform(get("/products/id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeProductById_returns200_andDelegatesToService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/products/id").param("id", id.toString()))
                .andExpect(status().isOk());

        verify(productsService, times(1)).removeProduct(eq(id));
    }

    @Test
    void removeProductById_returns404_whenServiceThrowsProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ProductNotFoundException("Product not found"))
                .when(productsService).removeProduct(id);

        mockMvc.perform(delete("/products/id").param("id", id.toString()))
                .andExpect(status().isNotFound());
    }
}
