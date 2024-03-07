package za.co.example.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import za.co.example.persistance.entities.Product;


@Component
@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping
    Product getProduct(@RequestParam("productName") String productName);
}

