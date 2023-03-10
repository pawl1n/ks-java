package ua.kishkastrybaie.order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
  OrderItemDto toDto(OrderItem cartItem);
}
