package ua.kishkastrybaie.cart;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
  CartDto toDto(Cart cart);
}
