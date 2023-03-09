package ua.kishkastrybaie.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.controller.OrderController;
import ua.kishkastrybaie.controller.dto.OrderItemDto;
import ua.kishkastrybaie.controller.dto.mapper.OrderItemMapper;
import ua.kishkastrybaie.repository.entity.OrderItem;

@Component
@RequiredArgsConstructor
public class OrderItemModelAssembler
    implements RepresentationModelAssembler<OrderItem, OrderItemDto> {
  private final OrderItemMapper orderItemMapper;

  @Override
  @NonNull
  public OrderItemDto toModel(@NonNull OrderItem orderItem) {
    return orderItemMapper
        .toDto(orderItem)
        .add(linkTo(methodOn(OrderController.class).one(orderItem.getId())).withSelfRel());
  }

  @Override
  @NonNull
  public CollectionModel<OrderItemDto> toCollectionModel(
      @NonNull Iterable<? extends OrderItem> orderItems) {
    return RepresentationModelAssembler.super
        .toCollectionModel(orderItems)
        .add(linkTo(methodOn(OrderController.class).all()).withSelfRel());
  }
}
