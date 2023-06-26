package ua.kishkastrybaie.order;

import org.mapstruct.*;
import ua.kishkastrybaie.order.item.OrderItemModelAssembler;

@Mapper(
    componentModel = "spring",
    uses = {OrderItemModelAssembler.class})
public interface OrderMapper {
  @Mapping(target = "currentStatus", source = "status")
  @Mapping(target = "fullName", source = "customerFullName")
  OrderDto toDto(Order order);
}
