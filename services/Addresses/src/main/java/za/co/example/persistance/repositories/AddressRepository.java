package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.example.persistance.entities.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByCity(String city);

    List<Address> findByStreetName(String streetName);

    List<Address> findByHouseNumber(String houseNumber);

    List<Address> findByZipCode(String zipCode);

    Address findByCityIgnoreCaseAndStreetNameIgnoreCaseAndHouseNumberIgnoreCaseAndZipCodeIgnoreCase(
            String city, String streetName, String houseNumber, String zipCode
    );

}
