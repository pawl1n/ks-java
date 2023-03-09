package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kishkastrybaie.controller.dto.CartDto;

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
