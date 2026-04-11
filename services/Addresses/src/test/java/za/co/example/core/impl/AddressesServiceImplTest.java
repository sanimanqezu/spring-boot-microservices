package za.co.example.core.impl;

import com.example.addresses_service.models.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.example.exceptions.AddressNotFoundException;
import za.co.example.exceptions.AddressesNotFoundException;
import za.co.example.persistance.entities.Address;
import za.co.example.persistance.repositories.AddressRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressesServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressesServiceImpl addressesService;

    private Address address;
    private String id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID().toString();
        address = new Address();
        address.setId(id);
        address.setCity("Cape Town");
        address.setStreetName("Main Road");
        address.setHouseNumber("12A");
        address.setZipCode("8001");
    }

    @Test
    void addAddress_withValidHouseNumber_savesAndReturns() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO dto = new AddressDTO()
                .city("Cape Town")
                .streetName("Main Road")
                .houseNumber("12A")
                .zipCode("8001");

        AddressDTO result = addressesService.addAddress(dto);

        assertThat(result).isNotNull();
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void addAddress_withNullHouseNumber_throwsAddressNotFoundException() {
        AddressDTO dto = new AddressDTO().city("Cape Town");

        assertThatThrownBy(() -> addressesService.addAddress(dto))
                .isInstanceOf(AddressNotFoundException.class)
                .hasMessageContaining("No house number found");
    }

    @Test
    void removeAddress_withExistingId_deletesAddress() {
        when(addressRepository.existsById(id)).thenReturn(true);
        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        addressesService.removeAddress(UUID.fromString(id));

        verify(addressRepository, times(1)).delete(any(Address.class));
    }

    @Test
    void removeAddress_withNonExistingId_throwsAddressNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(addressRepository.existsById(randomId.toString())).thenReturn(false);

        assertThatThrownBy(() -> addressesService.removeAddress(randomId))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    void getAllAddresses_withAddresses_returnsList() {
        when(addressRepository.findAll()).thenReturn(List.of(address));

        List<AddressDTO> result = addressesService.getAllAddresses();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllAddresses_withNoAddresses_throwsAddressesNotFoundException() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> addressesService.getAllAddresses())
                .isInstanceOf(AddressesNotFoundException.class)
                .hasMessageContaining("No address was found");
    }

    @Test
    void getAddressById_withExistingId_returnsAddress() {
        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        AddressDTO result = addressesService.getAddressById(UUID.fromString(id));

        assertThat(result).isNotNull();
    }

    @Test
    void getAddressById_withNonExistingId_throwsAddressNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(addressRepository.findById(randomId.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressesService.getAddressById(randomId))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    void getAddressesByCity_withResults_returnsList() {
        when(addressRepository.findByCity("Cape Town")).thenReturn(List.of(address));

        List<AddressDTO> result = addressesService.getAddressesByCity("Cape Town");

        assertThat(result).hasSize(1);
    }

    @Test
    void getAddressesByCity_withNoResults_throwsAddressesNotFoundException() {
        when(addressRepository.findByCity("Unknown")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> addressesService.getAddressesByCity("Unknown"))
                .isInstanceOf(AddressesNotFoundException.class);
    }
}
