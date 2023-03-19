package ua.kishkastrybaie.authentication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<TokenDto> register(@Valid @RequestBody RegisterRequest request) {
    log.info("Register request: {}", request.email());

    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> authenticate(@RequestBody AuthenticationRequest request) {
    log.info("Authenticate request: {}", request.email());

    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenDto> refresh(@RequestBody RefreshRequest request) {
    log.info("Refresh request");

    return ResponseEntity.ok(authenticationService.refresh(request));
  }
}
