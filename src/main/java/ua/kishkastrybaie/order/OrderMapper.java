package ua.kishkastrybaie.order;

import org.mapstruct.*;
import ua.kishkastrybaie.order.item.OrderItemMapper;

@Mapper(
    componentModel = "spring",
    uses = {OrderItemMapper.class})
public interface OrderMapper {
  @Mapping(target = "currentStatus", source = "status")
  OrderDto toDto(Order order);
}
