package za.co.example.controllers;

import com.example.addresses_service.models.AddressDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import za.co.example.core.services.IAddressesService;
import za.co.example.exceptions.AddressNotFoundException;
import za.co.example.exceptions.AddressesNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressesController.class)
class AddressesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAddressesService addressesService;

    @Test
    void addAddress_returns201AndBody_andDelegatesToService() throws Exception {
        AddressDTO body = new AddressDTO().city("Cape Town").streetName("Long Street")
                .houseNumber("42").zipCode("8001");
        when(addressesService.addAddress(any(AddressDTO.class))).thenReturn(body);

        mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Cape Town"));

        verify(addressesService, times(1)).addAddress(any(AddressDTO.class));
    }

    @Test
    void getAllAddresses_returns200AndBody() throws Exception {
        when(addressesService.getAllAddresses())
                .thenReturn(List.of(new AddressDTO().city("Cape Town")));

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Cape Town"));
    }

    @Test
    void getAddressById_returns200_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(addressesService.getAddressById(id))
                .thenReturn(new AddressDTO().id(id).city("Cape Town"));

        mockMvc.perform(get("/addresses/id").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Cape Town"));
    }

    @Test
    void getAddressById_returns404_whenServiceThrowsAddressNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(addressesService.getAddressById(id))
                .thenThrow(new AddressNotFoundException("Address not found"));

        mockMvc.perform(get("/addresses/id").param("id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    void getAllAddresses_returns404_whenServiceThrowsAddressesNotFound() throws Exception {
        when(addressesService.getAllAddresses())
                .thenThrow(new AddressesNotFoundException("No address was found"));

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No address was found"));
    }

    @Test
    void getAddressById_returns400_whenIdParamMissing() throws Exception {
        mockMvc.perform(get("/addresses/id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeAddressById_returns200_andDelegatesToService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/addresses/id").param("id", id.toString()))
                .andExpect(status().isOk());

        verify(addressesService, times(1)).removeAddress(eq(id));
    }

    @Test
    void removeAddressById_returns404_whenServiceThrowsAddressNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new AddressNotFoundException("Address not found"))
                .when(addressesService).removeAddress(id);

        mockMvc.perform(delete("/addresses/id").param("id", id.toString()))
                .andExpect(status().isNotFound());
    }
}
