package ua.kishkastrybaie.order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.order.item.OrderItemController;

@Component
@RequiredArgsConstructor
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderDto> {
  private final OrderMapper orderMapper;

  @Override
  @NonNull
  public OrderDto toModel(@NonNull Order entity) {
    return orderMapper
        .toDto(entity)
        .add(linkTo(methodOn(OrderController.class).one(entity.getId())).withSelfRel())
        .add(linkTo(methodOn(OrderItemController.class).all(entity.getId(), null)).withRel("items"));
  }

  @Override
  @NonNull
  public CollectionModel<OrderDto> toCollectionModel(@NonNull Iterable<? extends Order> entities) {
    return RepresentationModelAssembler.super
        .toCollectionModel(entities)
        .add(linkTo(methodOn(OrderController.class).all(null)).withSelfRel());
  }
}
