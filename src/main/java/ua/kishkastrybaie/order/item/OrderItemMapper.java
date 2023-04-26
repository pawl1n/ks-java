package ua.kishkastrybaie.order.item;

import org.mapstruct.Mapper;
import ua.kishkastrybaie.product.item.ProductItemMapper;

@Mapper(
    componentModel = "spring",
    uses = {ProductItemMapper.class})
public interface OrderItemMapper {
  OrderItemDto toDto(OrderItem orderItem);
}
