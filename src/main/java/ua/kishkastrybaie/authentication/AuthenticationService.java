package ua.kishkastrybaie.authentication;

public interface AuthenticationService {
  TokenDto register(RegisterRequest request);

  TokenDto authenticate(AuthenticationRequest request);

  TokenDto refresh(RefreshRequest request);
}
