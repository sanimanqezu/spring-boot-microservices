package za.co.example.controllers;

import com.example.addresses_service.api.AddressesApi;
import com.example.addresses_service.models.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import za.co.example.core.services.IAddressesService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AddressesController implements AddressesApi{

    private final Logger logger = LoggerFactory.getLogger(AddressesController.class);

    private final IAddressesService addressesService;


    @Override
    public ResponseEntity<Void> addAddress(AddressDTO body) {
        logger.info("Adding address: {}", body.toString());
        addressesService.addAddress(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByCity(String city) {
        logger.info("Retrieving address(es) by city: {}", city);
        return ResponseEntity.ok(addressesService.getAddressesByCity(city));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByHouseNumber(String houseNumber) {
        logger.info("Retrieving address(es) by house number: {}", houseNumber);
        return ResponseEntity.ok(addressesService.getAddressesByHouseNumber(houseNumber));
    }

    @Override
    public ResponseEntity<AddressDTO> getAddressById(UUID id) {
        logger.info("Retrieving a address by id: {}", id);
        return ResponseEntity.ok(addressesService.getAddressById(id));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByStreetName(String streetName) {
        logger.info("Retrieving address(es) by street name: {}", streetName);
        return ResponseEntity.ok(addressesService.getAddressesByStreetName(streetName));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAddressByZipCode(String zipCode) {
        logger.info("Retrieving address(es) by zip code: {}", zipCode);
        return ResponseEntity.ok(addressesService.getAddressesByZipCode(zipCode));
    }

    @Override
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        logger.info("Retrieving all addresses");
        return ResponseEntity.ok(addressesService.getAllAddresses());
    }

    @Override
    public ResponseEntity<Void> removeAddressById(UUID id) {
        logger.info("Removing an address by Id: {}", id);
        addressesService.removeAddress(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<AddressDTO>> searchAddresses( UUID id, String city, String streetName, String houseNumber, String zipCode) {
        logger.info("Searching user(s) by the following properties: \n Id: {} \n City: {} \n Street Name: {} \n House Number: {} \n Zip Code: {}",
                id, city, streetName,houseNumber, zipCode);
        return ResponseEntity.ok(addressesService.searchAddresses(id, city, streetName, houseNumber, zipCode));
    }

    @Override
    public ResponseEntity<Void> updateAddressById(UUID id, AddressDTO body) {
        logger.info("Updating address identified by id: {}, with address: {}", id, body);
        addressesService.updateAddress(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<AddressDTO> getAddressByAddressArgs(String city, String streetName, String houseNumber, String zipCode) {
        logger.info("Getting address by the following properties: \n City: {} \n Street Name: {} \n House Number: {} \n Zip Code: {}",
                city, streetName, houseNumber, zipCode);
        return ResponseEntity.ok(addressesService.getAddressAddressArgs(city, streetName, houseNumber, zipCode));
    }
}
