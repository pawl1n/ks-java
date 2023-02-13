package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.ProductItemDto;
import ua.kishkastrybaie.repository.entity.ProductItem;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductItemMapper {
    ProductItemDto toDto(ProductItem productItem);
    List<ProductItemDto> toDto(List<ProductItem> productItems);
    ProductItem toDomain(ProductItemDto productItemDto);
}
