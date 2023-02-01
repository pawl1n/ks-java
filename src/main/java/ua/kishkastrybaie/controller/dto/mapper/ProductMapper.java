package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.controller.dto.ProductDto;
import ua.kishkastrybaie.repository.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.name", target = "category")
    ProductDto toDto(Product product);
    List<ProductDto> toDto(List<Product> products);
    @Mapping(source = "category", target = "category.name")
    Product toDomain(ProductDto productDto);
}
