package za.co.example.proxy;

import com.example.users_service.models.AddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "address-service")
public interface AddressFeignClient {

    @GetMapping(value = "/addresses/address", produces = MediaType.APPLICATION_JSON_VALUE)
    AddressDTO getAddress(@RequestParam("city") String city,
                          @RequestParam("streetName") String streetName,
                          @RequestParam("houseNumber") String houseNumber,
                          @RequestParam("zipCode") String zipCode);

    @PostMapping(value = "/addresses/address", produces = MediaType.APPLICATION_JSON_VALUE)
    AddressDTO saveAddress(@RequestBody AddressDTO address);
}

