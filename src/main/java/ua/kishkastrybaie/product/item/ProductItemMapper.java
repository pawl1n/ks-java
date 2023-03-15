package ua.kishkastrybaie.product.item;

import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring")
public interface ProductItemMapper {

  ProductItemDto toDto(ProductItem productItem);
}
