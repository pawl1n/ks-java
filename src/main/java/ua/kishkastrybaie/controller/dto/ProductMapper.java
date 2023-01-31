package ua.kishkastrybaie.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.repository.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "category")
    ProductDTO toDTO(Product product);

    @Mapping(source = "category", target = "category.name")
    Product toDomain(ProductDTO productDTO);
}
