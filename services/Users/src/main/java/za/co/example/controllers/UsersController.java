package za.co.example.controllers;

import com.example.users_service.api.UsersApi;
import com.example.users_service.models.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import za.co.example.core.services.IUsersService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UsersController implements UsersApi{

    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final IUsersService usersService;

    @Override
    public ResponseEntity<Void> addUser(UserDTO user) {
        logger.info("Adding user: {}", user.toString());
        usersService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Retrieving all users");
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID id) {
        logger.info("Retrieving a user by id: {}", id);
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByAddressId(String addressId) {
        logger.info("Retrieving user(s) by address: {}", addressId);
        return ResponseEntity.ok(usersService.getUsersByAddressId(addressId));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByDateOfBirth(LocalDate dateOfBirth) {
        logger.info("Retrieving user(s) by date of birth {}", dateOfBirth);
        return ResponseEntity.ok(usersService.getUsersByDateOfBirth(dateOfBirth));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByFirstName(String firstName) {
        logger.info("Retrieving user(s) by first name: {}", firstName);
        return ResponseEntity.ok(usersService.getUsersByFirstName(firstName));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByLastName(String lastName) {
        logger.info("Retrieving user(s) by last name: {}", lastName);
        return ResponseEntity.ok(usersService.getUsersByLastName(lastName));
    }

    @Override
    public ResponseEntity<UserDTO> getUsersByRsaId(String rsaId) {
        logger.info("Retrieving user by rsa id: {}", rsaId);
        return ResponseEntity.ok(usersService.getUserByRsaId(rsaId));
    }

    @Override
    public ResponseEntity<Void> removeUserById(UUID id) {
        logger.info("Removing a user by Id: {}", id);
        usersService.removeUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> updateUserById(UUID id, UserDTO updatedUser) {
        logger.info("Updating user identified by id: {}, with user: {}", id, updatedUser);
        usersService.updateUser(id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<UserDTO>> searchUsers(UUID id, String firstName, String lastName, LocalDate dateOfBirth,
                                                     String address, String rsaId) {
        logger.info("Searching user(s) by the following properties: \n Id: {} \n First Name: {} \n Last Name: {} \n Address: {} \n Date Of Birth: {} \n RSA Id: {} ",
                id, firstName, lastName,address, dateOfBirth, rsaId);
        return ResponseEntity.ok(usersService.searchUsers(id, firstName, lastName, address, dateOfBirth, rsaId));
    }
}
