package za.co.example.core.impl;

import com.example.users_service.models.AddressDTO;
import com.example.users_service.models.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.persistance.entities.User;
import za.co.example.persistance.repositories.UserRepository;
import za.co.example.proxy.AddressFeignClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressFeignClient addressFeignClient;

    @InjectMocks
    private UsersServiceImpl usersService;

    private User user;
    private String id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID().toString();
        user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRsaId("1234567890123");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
    }

    @Test
    void addUser_withValidRsaId_savesUser() {
        AddressDTO addressDTO = new AddressDTO()
                .city("Cape Town").streetName("Main").houseNumber("1").zipCode("8001");
        UserDTO userDTO = new UserDTO()
                .firstName("John").lastName("Doe")
                .rsaId("1234567890123").address(addressDTO);

        when(addressFeignClient.getAddress(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(addressDTO);
        when(userRepository.save(any(User.class))).thenReturn(user);

        usersService.addUser(userDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void addUser_withInvalidRsaId_throwsUserNotFoundException() {
        AddressDTO addressDTO = new AddressDTO()
                .city("Cape Town").streetName("Main").houseNumber("1").zipCode("8001");
        UserDTO userDTO = new UserDTO()
                .firstName("John").lastName("Doe")
                .rsaId("123").address(addressDTO);

        assertThatThrownBy(() -> usersService.addUser(userDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("RSA Id must have 13 digits");
    }

    @Test
    void removeUser_withExistingId_deletesUser() {
        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        usersService.removeUser(UUID.fromString(id));

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void removeUser_withNonExistingId_throwsUserNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.existsById(randomId.toString())).thenReturn(false);

        assertThatThrownBy(() -> usersService.removeUser(randomId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUsers_withUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = usersService.getAllUsers();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllUsers_withNoUsers_throwsUsersNotFoundException() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> usersService.getAllUsers())
                .isInstanceOf(UsersNotFoundException.class)
                .hasMessageContaining("No user was found");
    }

    @Test
    void getUserById_withExistingId_returnsUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO result = usersService.getUserById(UUID.fromString(id));

        assertThat(result).isNotNull();
    }

    @Test
    void getUserById_withNonExistingId_throwsUserNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usersService.getUserById(randomId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserByRsaId_withExistingRsaId_returnsUser() {
        when(userRepository.findByRsaId("1234567890123")).thenReturn(user);

        UserDTO result = usersService.getUserByRsaId("1234567890123");

        assertThat(result).isNotNull();
    }

    @Test
    void getUserByRsaId_withUnknownRsaId_throwsUserNotFoundException() {
        when(userRepository.findByRsaId("0000000000000")).thenReturn(null);

        assertThatThrownBy(() -> usersService.getUserByRsaId("0000000000000"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void searchUsers_withNoResults_throwsUsersNotFoundException() {
        assertThatThrownBy(() -> usersService.searchUsers(null, null, null, null, null, null))
                .isInstanceOf(UsersNotFoundException.class)
                .hasMessageContaining("No users were found");
    }

    @Test
    void searchUsers_withNoTrailingZero_messageIsCorrect() {
        assertThatThrownBy(() -> usersService.searchUsers(null, null, null, null, null, null))
                .isInstanceOf(UsersNotFoundException.class)
                .hasMessage("No users were found");
    }
}
