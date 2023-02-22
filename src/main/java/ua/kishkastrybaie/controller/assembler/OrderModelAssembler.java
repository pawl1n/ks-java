package ua.kishkastrybaie.controller.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.controller.OrderController;
import ua.kishkastrybaie.controller.dto.OrderDto;
import ua.kishkastrybaie.controller.dto.mapper.OrderMapper;
import ua.kishkastrybaie.repository.entity.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderDto> {
    private final OrderMapper orderMapper;

    @Override
    @NonNull
    public OrderDto toModel(@NonNull Order order) {
        return orderMapper.toDto(order)
                .add(linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel())
                .add(linkTo(methodOn(OrderController.class).orderItems(order.getId())).withRel("order-items"));
    }

    @Override
    @NonNull
    public CollectionModel<OrderDto> toCollectionModel(@NonNull Iterable<? extends Order> orders) {
        return RepresentationModelAssembler.super.toCollectionModel(orders)
                .add(linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }
}