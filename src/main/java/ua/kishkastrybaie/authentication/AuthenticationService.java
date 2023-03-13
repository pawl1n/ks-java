package ua.kishkastrybaie.authentication;

public interface AuthenticationService {
  AuthenticationDto register(RegisterRequest request);

  AuthenticationDto authenticate(AuthenticationRequest request);
}
