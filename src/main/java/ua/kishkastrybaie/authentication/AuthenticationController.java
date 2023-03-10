package ua.kishkastrybaie.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    log.info("Register request: {}", request.email());

    return ResponseEntity.ok(authenticationService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    log.info("Authenticate request: {}", request.email());

    return ResponseEntity.ok(authenticationService.authenticate(request));
  }
}
