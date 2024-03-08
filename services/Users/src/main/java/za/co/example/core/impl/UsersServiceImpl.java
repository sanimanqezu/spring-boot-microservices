package za.co.example.core.impl;

import com.example.users_service.models.AddressDTO;
import com.example.users_service.models.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.LocalDate;
import za.co.example.core.services.IUsersService;
import za.co.example.exceptions.UserNotFoundException;
import za.co.example.exceptions.UsersNotFoundException;
import za.co.example.mappers.UserMapper;
import za.co.example.persistance.repositories.UserRepository;
import za.co.example.proxy.AddressFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements IUsersService {

    private final UserRepository userRepository;

    private final AddressFeignClient addressFeignClient;

    @Transactional
    @Override
    public void addUser(UserDTO userDTO) {
        String rsaId = userDTO.getRsaId();

        AddressDTO addressDTO = userDTO.getAddress();

        if (rsaId.length() != 13) {
            throw new UserNotFoundException("RSA Id must have 13 digits");
        }

        AddressDTO existingAddress = UserMapper.USER_MAPPER.addressToAddressDTO(
                addressFeignClient.getAddress(
                addressDTO.getCity(),
                addressDTO.getStreetName(),
                addressDTO.getHouseNumber(),
                addressDTO.getZipCode()));

        if (existingAddress == null) {
            throw new UserNotFoundException("Address not found");
        }

        userDTO.setAddress(existingAddress);

        userRepository.save(UserMapper.USER_MAPPER.userDTOToUser(userDTO));
    }

    @Override
    public void removeUser(UUID id) {
        boolean user = userRepository.existsById(id);

        if (!user) {
            throw new UserNotFoundException("Id", id);
        }
        userRepository.delete(UserMapper.USER_MAPPER.userDTOToUser(getUserById(id)));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findAll());

        if (users.isEmpty()) {
            throw new UsersNotFoundException("No user was found!!");
        }
        return users;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        UserDTO userOptional = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findById(id).get());

        if (userOptional == null) {
            throw new UserNotFoundException("Id", id);
        }
        return userOptional;
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<UserDTO> users = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByFirstNameIgnoreCase(firstName));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("First Name", firstName);
        }
        return users;
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) {
        List<UserDTO> users = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByLastNameIgnoreCase(lastName));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("Last Name", lastName);
        }
        return users;
    }

    @Override
    public UserDTO getUserByRsaId(String rsaId) {
        UserDTO userDTO = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByRsaId(rsaId));
        if (userDTO == null) {
            throw new UserNotFoundException("RSA ID", rsaId);
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> getUsersByDateOfBirth(LocalDate dateOfBirth) {
        List<UserDTO> users = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByDateOfBirth(dateOfBirth));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("Date Of Birth", dateOfBirth);
        }
        return users;
    }

    @Override
    public List<UserDTO> getUsersByAddressId(String address) {
        List<UserDTO> users = UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByAddressId(address));
        if (users == null || users.isEmpty()) {
            throw new UsersNotFoundException("Address", address);
        }
        return users;
    }

    @Override
    public void updateUser(UUID id, UserDTO updatedUser) {
        UserDTO existingUser = getUserById(id);
        if (existingUser != null && updatedUser != null) {
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) existingUser.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) existingUser.setLastName(updatedUser.getLastName());
            if (updatedUser.getRsaId() != null && !updatedUser.getRsaId().isEmpty()) existingUser.setRsaId(updatedUser.getRsaId());
            if (updatedUser.getDateOfBirth() != null ) existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
            userRepository.save(UserMapper.USER_MAPPER.userDTOToUser(existingUser));
        }
    }

    @Override
    public List<UserDTO> searchUsers(UUID id, String firstName, String lastName, String rsaId, LocalDate dateOfBirth, String address) {
        List<UserDTO> users = new ArrayList<>();
        if (id != null) {
            users.add(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findById(id).get()));
        }
        if (firstName != null && !firstName.isEmpty()) {
            users.addAll(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByFirstNameIgnoreCase(firstName)));
        }
        if (lastName != null && !lastName.isEmpty()) {
            users.addAll(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByLastNameIgnoreCase(lastName)));
        }
        if (dateOfBirth != null ) {
            users.addAll(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByDateOfBirth(dateOfBirth)));
        }
        if (address != null && !address.isEmpty()) {
            users.addAll(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByAddressId(address)));
        }
        if (rsaId != null && !rsaId.isEmpty()) {
            users.add(UserMapper.USER_MAPPER.userToUserDTO(userRepository.findByRsaId(rsaId)));
        }

        if (users.isEmpty()){
            throw new UsersNotFoundException("No users were found0");
        }
        return users;
    }
}
