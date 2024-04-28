package ua.kishkastrybaie.order;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;
import ua.kishkastrybaie.order.item.OrderItem;
import ua.kishkastrybaie.order.item.OrderItemModelAssembler;
import ua.kishkastrybaie.product.Product;
import ua.kishkastrybaie.product.item.ProductItem;

@Mapper(
    componentModel = "spring",
    uses = {OrderItemModelAssembler.class})
public interface OrderMapper {
  @Mapping(target = "currentStatus", source = "status")
  @Mapping(target = "description", source = "items", qualifiedByName = "mapItems")
  OrderDto toDto(Order order);

  @Named("mapItems")
  default String mapItems(List<OrderItem> items) {
    return items.stream().map(OrderItem::getProductItem).map(ProductItem::getProduct).map(Product::getName).collect(
            Collectors.joining(", "));
  }
}
