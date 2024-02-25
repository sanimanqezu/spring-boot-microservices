package za.co.example.mappers;

import com.example.users_service.models.AddressDTO;
import com.example.users_service.models.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Address;
import za.co.example.persistance.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class) ;

    UserDTO userToUserDTO(User user);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "rsaId", target = "rsaId")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> userToUserDTO(List<User> users);

}
