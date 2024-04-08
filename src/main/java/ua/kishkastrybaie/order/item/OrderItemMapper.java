package ua.kishkastrybaie.order.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.product.item.ProductItemMapper;

@Mapper(
    componentModel = "spring",
    uses = {ProductItemMapper.class})
public interface OrderItemMapper {
  @Mapping(target = "productName", source = "productItem.product.name")
  @Mapping(target = "sku", source = "productItem.sku")
  OrderItemDto toDto(OrderItem orderItem);
}
