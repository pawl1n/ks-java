package ua.kishkastrybaie.authentication;

import org.springframework.security.core.Authentication;

public interface TokenService {
  String generateToken(Authentication authentication);
}
