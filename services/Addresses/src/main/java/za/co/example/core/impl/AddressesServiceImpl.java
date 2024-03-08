package za.co.example.core.impl;

import com.example.addresses_service.models.AddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.example.core.services.IAddressesService;
import za.co.example.exceptions.AddressNotFoundException;
import za.co.example.exceptions.AddressesNotFoundException;
import za.co.example.mappers.AddressMapper;
import za.co.example.persistance.repositories.AddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AddressesServiceImpl implements IAddressesService {

    private final AddressRepository addressRepository;

    public AddressesServiceImpl(AddressRepository addressRepository) {this.addressRepository = addressRepository;
    }

    @Override
    public void addAddress(AddressDTO addressDTO) {
        String houseNumber = addressDTO.getHouseNumber();

        if (houseNumber == null) {
            throw new AddressNotFoundException("No house number found");
        }
        addressRepository.save(AddressMapper.ADDRESS_MAPPER.dtoToEntity(addressDTO));
    }

    @Override
    public void removeAddress(UUID id) {
        boolean address = addressRepository.existsById(id);

        if (!address) {
            throw new AddressNotFoundException("Id", id);
        }
        addressRepository.delete(AddressMapper.ADDRESS_MAPPER.dtoToEntity(getAddressById(id)));

    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<AddressDTO> addresses = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findAll());
        if (addresses.isEmpty()) {
            throw new AddressesNotFoundException("No address was found!!");
        }
        return addresses;
    }

    @Override
    public AddressDTO getAddressById(UUID id) {
        AddressDTO AddressOptional = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findById(id).get());

        if (AddressOptional == null) {
            throw new AddressNotFoundException("Id", id);
        }
        return AddressOptional;
    }

    @Override
    public List<AddressDTO> getAddressesByCity(String city) {
        List<AddressDTO> addresses = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByCity(city));
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressesNotFoundException("City Name", city);
        }
        return addresses;
    }

    @Override
    public List<AddressDTO> getAddressesByStreetName(String streetName) {
        List<AddressDTO> addresses = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByStreetName(streetName));
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressesNotFoundException("Street Name", streetName);
        }
        return addresses;
    }

    @Override
    public List<AddressDTO> getAddressesByHouseNumber(String houseNumber) {
        List<AddressDTO> addresses = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByHouseNumber(houseNumber));
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressesNotFoundException("House Name", houseNumber);
        }
        return addresses;
    }

    @Override
    public List<AddressDTO> getAddressesByZipCode(String zipCode) {
        List<AddressDTO> addresses = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByZipCode(zipCode));
        if (addresses == null || addresses.isEmpty()) {
            throw new AddressesNotFoundException("Zip Code", zipCode);
        }
        return addresses;
    }

    @Override
    public void updateAddress(UUID id, AddressDTO updatedAddress) {
        AddressDTO existingAddress = getAddressById(id);
        if (existingAddress != null && updatedAddress != null) {
            if (updatedAddress.getCity() != null && !updatedAddress.getCity().isEmpty()) existingAddress.setCity(updatedAddress.getCity());
            if (updatedAddress.getStreetName() != null && !updatedAddress.getStreetName().isEmpty()) existingAddress.setStreetName(updatedAddress.getStreetName());
            if (updatedAddress.getHouseNumber() != null && !updatedAddress.getHouseNumber().isEmpty()) existingAddress.setHouseNumber(updatedAddress.getHouseNumber());
            if (updatedAddress.getZipCode() != null && !updatedAddress.getZipCode().isEmpty() ) existingAddress.setZipCode(updatedAddress.getZipCode());
            addressRepository.save(AddressMapper.ADDRESS_MAPPER.dtoToEntity(updatedAddress));
        }
    }

    @Override
    public AddressDTO getAddressAddressArgs(String city, String streetName, String houseNumber, String zipCode) {
        AddressDTO existingAddressDTO = AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository
                .findByCityIgnoreCaseAndStreetNameIgnoreCaseAndHouseNumberIgnoreCaseAndZipCodeIgnoreCase(city, streetName, houseNumber,zipCode));
        log.info("Existing Address: {}", existingAddressDTO != null ? existingAddressDTO : "Address does not exist");
        if (existingAddressDTO != null) {
            return existingAddressDTO;
        } else {
            AddressDTO addressDTO = new AddressDTO()
                    .city(city)
                            .houseNumber(houseNumber)
                                    .streetName(streetName)
                                            .zipCode(zipCode);
            log.info("Saving address: " + addressDTO);
            return AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository
                    .save(AddressMapper.ADDRESS_MAPPER.dtoToEntity(addressDTO)));
        }
    }

    @Override
    public List<AddressDTO> searchAddresses(UUID id, String city, String streetName, String houseNumber, String zipCode) {
        List<AddressDTO> addresses = new ArrayList<>();
        if (id != null) {
            addresses.add(AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findById(id).get()));
        }
        if (city != null && !city.isEmpty()) {
            addresses.addAll(AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByCity(city)));
        }
        if (streetName != null && !streetName.isEmpty()) {
            addresses.addAll(AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByStreetName(streetName)));
        }
        if (houseNumber != null && !houseNumber.isEmpty()) {
            addresses.addAll(AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByHouseNumber(houseNumber)));
        }
        if (zipCode != null && !zipCode.isEmpty()) {
            addresses.addAll(AddressMapper.ADDRESS_MAPPER.entityToDto(addressRepository.findByZipCode(zipCode)));
        }
        return addresses;
    }
}
