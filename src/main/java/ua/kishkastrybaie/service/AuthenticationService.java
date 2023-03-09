package ua.kishkastrybaie.service;

import ua.kishkastrybaie.controller.dto.AuthenticationRequest;
import ua.kishkastrybaie.controller.dto.AuthenticationResponse;
import ua.kishkastrybaie.controller.dto.RegisterRequest;

public interface AuthenticationService {
  AuthenticationResponse register(RegisterRequest request);

  AuthenticationResponse authenticate(AuthenticationRequest request);
}
