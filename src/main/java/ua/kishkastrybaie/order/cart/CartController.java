package ua.kishkastrybaie.order.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
  // TODO: implement cart controller
  @GetMapping("/{id}")
  public ResponseEntity<CartDto> one(@PathVariable Long id) {
    return ResponseEntity.ok().build();
  }
}
