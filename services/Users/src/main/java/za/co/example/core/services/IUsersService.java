package za.co.example.core.services;

import com.example.users_service.models.UserDTO;
import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.UUID;

public interface IUsersService {

    void addUser(UserDTO userDTO);

    void removeUser(UUID id);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    List<UserDTO> getUsersByFirstName(String firstName);

    List<UserDTO> getUsersByLastName(String lastName);

    UserDTO getUserByRsaId(String rsaId);

    List<UserDTO> getUsersByDateOfBirth(LocalDate dateOfBirth);

    List<UserDTO> getUsersByAddressId(String address);

    void updateUser(UUID id, UserDTO updatedUser);

    List<UserDTO> searchUsers(UUID id, String firstName, String lastName, String rsaId, LocalDate dateOfBirth, String address);
}
