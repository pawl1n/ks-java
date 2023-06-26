package ua.kishkastrybaie.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.order.status.OrderStatus;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<CollectionModel<OrderDto>> all(@PageableDefault Pageable pageable) {
    log.info("Get all orders");
    return ResponseEntity.ok(orderService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> one(@PathVariable Long id) {
    log.info("Get order by id {}", id);
    return ResponseEntity.ok(orderService.findById(id));
  }

  @PostMapping
  public ResponseEntity<OrderDto> save(@RequestBody OrderRequestDto orderRequest) {
    log.info("Save order {}", orderRequest);
    OrderDto responseDto = orderService.save(orderRequest);
    return ResponseEntity.created(responseDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(responseDto);
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<OrderDto> changeStatus(
      @PathVariable Long id, @Valid @RequestBody OrderStatus status) {
    log.info("Change order status {}", status);
    return ResponseEntity.ok(orderService.changeStatus(id, status));
  }
}
