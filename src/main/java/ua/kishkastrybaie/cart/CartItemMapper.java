package ua.kishkastrybaie.cart;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
  @Mapping(source = "productItem.id", target = "productItemId")
  @Mapping(source = "productItem.product.name", target = "productName")
  @Mapping(source = "productItem.product.mainImage.url", target = "productImage")
  @Mapping(source = "productItem.price", target = "productItemPrice")
  CartItemDto toDto(CartItem cartItem);
}
