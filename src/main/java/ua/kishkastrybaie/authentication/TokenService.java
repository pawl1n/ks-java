package ua.kishkastrybaie.authentication;

import org.springframework.security.core.Authentication;

public interface TokenService {
  TokenDto generateToken(Authentication authentication);
}
