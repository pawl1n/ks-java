package ua.kishkastrybaie.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser() {
    log.info("Get current user");
    UserDto userDto = userService.getCurrentUser();

    if (userDto == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }

    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me")
  public ResponseEntity<UserDto> update(@Valid @RequestBody UserRequestDto userRequestDto) {
    log.info("Update user");
    log.info("userRequest: {}", userRequestDto);
    UserDto userDto = userService.update(userRequestDto);

    if (userDto == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }

    return ResponseEntity.ok(userDto);
  }
}
