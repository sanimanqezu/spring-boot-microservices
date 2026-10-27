package za.co.example.controllers;

import com.example.users_service.models.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import za.co.example.core.services.IUsersService;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;

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

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUsersService usersService;

    @Test
    void addUser_returns201_andDelegatesToService() throws Exception {
        UserDTO body = new UserDTO().firstName("John").lastName("Doe").rsaId("1234567890123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        verify(usersService, times(1)).addUser(any(UserDTO.class));
    }

    @Test
    void getAllUsers_returns200AndBody() throws Exception {
        when(usersService.getAllUsers())
                .thenReturn(List.of(new UserDTO().firstName("John").lastName("Doe")));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getUserById_returns200_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(usersService.getUserById(id))
                .thenReturn(new UserDTO().id(id).firstName("John"));

        mockMvc.perform(get("/users/id").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getUserById_returns404_whenServiceThrowsUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(usersService.getUserById(id))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/id").param("id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void getAllUsers_returns404_whenServiceThrowsUsersNotFound() throws Exception {
        when(usersService.getAllUsers())
                .thenThrow(new UsersNotFoundException("No user was found"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No user was found"));
    }

    @Test
    void getUserById_returns400_whenIdParamMissing() throws Exception {
        mockMvc.perform(get("/users/id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeUserById_returns200_andDelegatesToService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/users/id").param("id", id.toString()))
                .andExpect(status().isOk());

        verify(usersService, times(1)).removeUser(eq(id));
    }

    @Test
    void removeUserById_returns404_whenServiceThrowsUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new UserNotFoundException("User not found"))
                .when(usersService).removeUser(id);

        mockMvc.perform(delete("/users/id").param("id", id.toString()))
                .andExpect(status().isNotFound());
    }
}
