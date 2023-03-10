package ua.kishkastrybaie.authentication;

public interface AuthenticationService {
  AuthenticationResponse register(RegisterRequest request);

  AuthenticationResponse authenticate(AuthenticationRequest request);
}
