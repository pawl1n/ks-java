package ua.kishkastrybaie.order.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.kishkastrybaie.variation.option.VariationOptionMapper;

@Mapper(componentModel = "spring", uses = VariationOptionMapper.class)
public interface OrderItemMapper {
  @Mapping(target = "variationOptions", source = "productItem.variationOptions")
  @Mapping(target = "productName", source = "productItem.product.name")
  @Mapping(target = "sku", source = "productItem.sku")
  OrderItemDto toDto(OrderItem orderItem);
}
