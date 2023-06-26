package ua.kishkastrybaie.order.item;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.order.OrderController;
import ua.kishkastrybaie.product.ProductController;

@Component
@RequiredArgsConstructor
public class OrderItemModelAssembler
    implements RepresentationModelAssembler<OrderItem, OrderItemDto> {
  private final OrderItemMapper orderItemMapper;

  @Override
  @NonNull
  public OrderItemDto toModel(@NonNull OrderItem entity) {
    return orderItemMapper
        .toDto(entity)
        .add(
            linkTo(
                    methodOn(ProductController.class)
                        .one(entity.getProductItem().getProduct().getId()))
                .withRel("productItem"));
  }

  @Override
  @NonNull
  public CollectionModel<OrderItemDto> toCollectionModel(
      @NonNull Iterable<? extends OrderItem> entities) {
    return RepresentationModelAssembler.super
        .toCollectionModel(entities)
        .add(linkTo(methodOn(OrderController.class).all(null)).withSelfRel());
  }
}
