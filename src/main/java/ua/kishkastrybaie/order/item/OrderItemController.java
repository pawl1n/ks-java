package ua.kishkastrybaie.order.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {
  private final OrderItemService orderItemService;

  @GetMapping
  public ResponseEntity<CollectionModel<OrderItemDto>> all(
      @PathVariable Long orderId, @PageableDefault Pageable pageable) {
    log.info("Get all order items by order id {}", orderId);

    return ResponseEntity.ok(orderItemService.findAllByOrderId(orderId, pageable));
  }
}
