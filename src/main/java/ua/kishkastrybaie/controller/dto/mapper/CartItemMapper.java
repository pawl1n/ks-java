package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.controller.dto.CartItemDto;
import ua.kishkastrybaie.repository.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "productItem.id", target = "productItemId")
    @Mapping(source = "productItem.product.name", target = "productName")
    @Mapping(source = "productItem.product.mainImage", target = "productImage")
    @Mapping(source = "productItem.price", target = "productItemPrice")
    CartItemDto toDto(CartItem cartItem);

}
