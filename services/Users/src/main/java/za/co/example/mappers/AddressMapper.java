package za.co.example.mappers;

import com.example.users_service.models.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Address;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

    AddressMapper ADDRESS_MAPPER = Mappers.getMapper(AddressMapper.class) ;

    AddressDTO addressToAddressDTO(Address address);

    List<AddressDTO> addressToAddressDTO(List<Address> addresses);

    Address addressDTOToAddress(AddressDTO addressDTO);

}
