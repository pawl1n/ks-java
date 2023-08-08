package ua.kishkastrybaie.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal User user) {
    log.info("Get current user");
    UserDto userDto = userService.getUserDetails(user);

    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me")
  public ResponseEntity<UserDto> update(
      @AuthenticationPrincipal User user, @Valid @RequestBody UserRequestDto userRequestDto) {
    log.info("Update user");
    log.info("userRequest: {}", userRequestDto);
    UserDto userDto = userService.update(user, userRequestDto);

    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me/password")
  public ResponseEntity<UserDto> changePassword(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
    log.info("Change password");
    UserDto userDto = userService.changePassword(user, changePasswordRequest);

    return ResponseEntity.ok(userDto);
  }
}
