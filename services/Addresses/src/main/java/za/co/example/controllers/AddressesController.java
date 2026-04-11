package za.co.example.controllers;

import com.example.addresses_service.api.AddressesApi;
import com.example.addresses_service.models.AddressDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import za.co.example.core.services.IAddressesService;

import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
public class AddressesController implements AddressesApi{

    private final IAddressesService addressesService;


    @Override
    public ResponseEntity<AddressDTO> addAddress(AddressDTO body) {
        log.info("Adding address: {}", body.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(addressesService.addAddress(body));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByCity(String city) {
        log.info("Retrieving address(es) by city: {}", city);
        return ResponseEntity.ok(addressesService.getAddressesByCity(city));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByHouseNumber(String houseNumber) {
        log.info("Retrieving address(es) by house number: {}", houseNumber);
        return ResponseEntity.ok(addressesService.getAddressesByHouseNumber(houseNumber));
    }

    @Override
    public ResponseEntity<AddressDTO> getAddressById(UUID id) {
        log.info("Retrieving a address by id: {}", id);
        return ResponseEntity.ok(addressesService.getAddressById(id));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByStreetName(String streetName) {
        log.info("Retrieving address(es) by street name: {}", streetName);
        return ResponseEntity.ok(addressesService.getAddressesByStreetName(streetName));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByZipCode(String zipCode) {
        log.info("Retrieving address(es) by zip code: {}", zipCode);
        return ResponseEntity.ok(addressesService.getAddressesByZipCode(zipCode));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        log.info("Retrieving all addresses");
        return ResponseEntity.ok(addressesService.getAllAddresses());
    }

    @Override
    public ResponseEntity<Void> removeAddressById(UUID id) {
        log.info("Removing an address by Id: {}", id);
        addressesService.removeAddress(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<AddressDTO>> searchAddresses( UUID id, String city, String streetName, String houseNumber, String zipCode) {
        log.info("Searching user(s) by the following properties: \n Id: {} \n City: {} \n Street Name: {} \n House Number: {} \n Zip Code: {}",
                id, city, streetName,houseNumber, zipCode);
        return ResponseEntity.ok(addressesService.searchAddresses(id, city, streetName, houseNumber, zipCode));
    }

    @Override
    public ResponseEntity<Void> updateAddressById(UUID id, AddressDTO body) {
        log.info("Updating address identified by id: {}, with address: {}", id, body);
        addressesService.updateAddress(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<AddressDTO> getAddressByAddressArgs(String city, String streetName, String houseNumber, String zipCode) {
        log.info("Getting address by the following properties: \n City: {} \n Street Name: {} \n House Number: {} \n Zip Code: {}",
                city, streetName, houseNumber, zipCode);
        return ResponseEntity.ok(addressesService.getAddressAddressArgs(city, streetName, houseNumber, zipCode));
    }
}
