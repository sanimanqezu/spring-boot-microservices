package za.co.example.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import za.co.example.persistance.entities.Address;

@Component
@FeignClient(name = "address-service")
public interface AddressFeignClient {

    @GetMapping("/address")
    Address getAddress(@RequestParam("city") String city,
                       @RequestParam("streetName") String streetName,
                       @RequestParam("houseNumber") String houseNumber,
                       @RequestParam("zipCode") String zipCode);
}

