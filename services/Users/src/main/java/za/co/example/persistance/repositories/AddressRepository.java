package za.co.example.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import za.co.example.persistance.entities.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query(value = "SELECT a FROM address a WHERE LOWER(a.city) = LOWER(:city) " +
            "AND LOWER(a.streetName) = LOWER(:streetName) " +
            "AND LOWER(a.houseNumber) = LOWER(:houseNumber) " +
            "AND LOWER(a.zipCode) = LOWER(:zipCode)", nativeQuery = true)
    Address findByCityAndStreetNameAndHouseNumberAndZipCode(@Param("city") String city, @Param("streetName") String streetName,
                                                            @Param("houseNumber") String houseNumber, @Param("zipCode") String zipCode);
}
