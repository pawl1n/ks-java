package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.CartDto;
import ua.kishkastrybaie.repository.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
  CartDto toDto(Cart cart);
}
