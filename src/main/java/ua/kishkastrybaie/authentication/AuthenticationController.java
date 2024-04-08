package ua.kishkastrybaie.authentication;

import jakarta.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
    log.info("Register request: {}", request.email());

    return ResponseEntity.noContent()
        .headers(tokenHeaders(authenticationService.register(request)))
        .build();
  }

  @PostMapping("/login")
  public ResponseEntity<Void> authenticate(@RequestBody AuthenticationRequest request) {
    log.info("Authenticate request: {}", request.email());

    return ResponseEntity.noContent()
        .headers(tokenHeaders(authenticationService.authenticate(request)))
        .build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<Void> refresh(@RequestBody RefreshRequest request) {
    log.info("Refresh request");

    return ResponseEntity.noContent()
        .headers(tokenHeaders(authenticationService.refresh(request)))
        .build();
  }

  private HttpHeaders tokenHeaders(TokenDto tokenDto) {
    ResponseCookie accessToken =
        ResponseCookie.from("accessToken", tokenDto.accessToken())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond())
            .build();
    ResponseCookie refreshToken =
        ResponseCookie.from("refreshToken", tokenDto.refreshToken())
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Instant.now().plus(30, ChronoUnit.DAYS).getEpochSecond())
            .build();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Set-Cookie", accessToken.toString());
    httpHeaders.add("Set-Cookie", refreshToken.toString());
    return httpHeaders;
  }
}
