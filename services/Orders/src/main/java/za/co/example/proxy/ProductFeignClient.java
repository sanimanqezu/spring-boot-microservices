package za.co.example.proxy;

import com.example.orders_service.models.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping(value = "/products/productName", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDTO getProduct(@RequestParam("productName") String productName);

    @PutMapping(value = "/products/id", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDTO updateProduct(@RequestParam(value = "id") UUID id, @RequestBody ProductDTO body);
}

