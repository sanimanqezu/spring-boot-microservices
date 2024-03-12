package za.co.example.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import za.co.example.persistance.entities.Address;

@FeignClient(name = "address-service")
public interface AddressFeignClient {

    @GetMapping(value = "/addresses/address", produces = MediaType.APPLICATION_JSON_VALUE)
    Address getAddress(@RequestParam("city") String city,
                       @RequestParam("streetName") String streetName,
                       @RequestParam("houseNumber") String houseNumber,
                       @RequestParam("zipCode") String zipCode);

    @PostMapping(value = "/addresses/address", produces = MediaType.APPLICATION_JSON_VALUE)
    Address saveAddress(@RequestBody Address address);
}

