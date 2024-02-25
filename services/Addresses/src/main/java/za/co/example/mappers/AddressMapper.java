package za.co.example.mappers;

import com.example.addresses_service.models.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Address;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper ADDRESS_MAPPER = Mappers.getMapper(AddressMapper.class) ;

    AddressDTO entityToDto(Address address);
    Address dtoToEntity(AddressDTO addressDTO);
    List<AddressDTO> entityToDto(List<Address> address);

}
