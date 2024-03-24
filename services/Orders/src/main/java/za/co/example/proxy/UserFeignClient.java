package za.co.example.proxy;

import com.example.orders_service.models.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping(value = "/users/rsaId", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO getUserByRsaId(@RequestParam("rsaId") String rsaId);
}

