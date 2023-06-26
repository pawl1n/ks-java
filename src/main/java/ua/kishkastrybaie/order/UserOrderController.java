package ua.kishkastrybaie.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.user.User;

@RestController
@RequestMapping("/api/users/me/orders")
@RequiredArgsConstructor
@Slf4j
public class UserOrderController {
  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<CollectionModel<OrderDto>> all(@AuthenticationPrincipal User user,
                                                       @PageableDefault Pageable pageable) {
    log.info("Get all orders");
    return ResponseEntity.ok(orderService.findAllByUser(pageable, user));
  }
}
