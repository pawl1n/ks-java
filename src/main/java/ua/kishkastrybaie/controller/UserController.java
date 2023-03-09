package ua.kishkastrybaie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kishkastrybaie.repository.entity.User;
import ua.kishkastrybaie.service.UserService;

// @RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<User> me() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @GetMapping
  public ResponseEntity<Iterable<User>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> one(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user) {
    return ResponseEntity.ok(userService.create(user));
  }
}
