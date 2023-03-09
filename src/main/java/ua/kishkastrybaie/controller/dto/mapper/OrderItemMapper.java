package ua.kishkastrybaie.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.controller.dto.OrderItemDto;
import ua.kishkastrybaie.repository.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
  OrderItemDto toDto(OrderItem cartItem);
}
