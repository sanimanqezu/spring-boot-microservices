package za.co.example.mappers;


import com.example.products_service.models.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Product;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper{

    ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class) ;

    ProductDTO entityToDto(Product product);

    Product dtoToEntity(ProductDTO productDTO);

    List<ProductDTO> entityToDto(List<Product> product);

}
