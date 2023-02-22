package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kishkastrybaie.controller.assembler.OrderItemModelAssembler;
import ua.kishkastrybaie.controller.assembler.OrderModelAssembler;
import ua.kishkastrybaie.controller.dto.OrderDto;
import ua.kishkastrybaie.controller.dto.OrderItemDto;
import ua.kishkastrybaie.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final OrderItemModelAssembler orderItemModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<OrderDto>> all() {
        CollectionModel<OrderDto> responseDto = orderModelAssembler.toCollectionModel(orderService.findAll());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> one(@PathVariable Long id) {
        OrderDto responseDto = orderModelAssembler.toModel(orderService.findById(id));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}/order-items")
    public ResponseEntity<CollectionModel<OrderItemDto>> orderItems(@PathVariable Long id) {
        CollectionModel<OrderItemDto> responseDto = orderItemModelAssembler.toCollectionModel(
                orderService.findById(id).getOrderItems()
        );

        return ResponseEntity.ok(responseDto);
    }
}
