package za.co.example.core.services;

import com.example.addresses_service.models.AddressDTO;

import java.util.List;
import java.util.UUID;

public interface IAddressesService {

    AddressDTO addAddress(AddressDTO addressDTO);

    void removeAddress(UUID id);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(UUID id);

    List<AddressDTO> getAddressesByCity(String city);

    List<AddressDTO> getAddressesByStreetName(String streetName);

    List<AddressDTO> getAddressesByHouseNumber(String houseNumber);

    List<AddressDTO> getAddressesByZipCode(String zipCode);

    void updateAddress(UUID id, AddressDTO updatedAddress);

    AddressDTO getAddressAddressArgs(String city, String streetName, String houseNumber, String zipCode);

    List<AddressDTO> searchAddresses(UUID id, String city, String streetName, String houseNumber, String zipCode);
}
