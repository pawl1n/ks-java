package ua.kishkastrybaie.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthenticationResponse register(RegisterRequest request) {
    User userDetails =
        User.builder()
            .firstName(request.firstName())
            .middleName(request.middleName())
            .lastName(request.lastName())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .phoneNumber(request.phoneNumber())
            .role(Role.USER)
            .build();

    userRepository.save(userDetails);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    return authenticationResponse(authentication);
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    return authenticationResponse(authentication);
  }

  private AuthenticationResponse authenticationResponse(Authentication authentication) {
    return new AuthenticationResponse(tokenService.generateToken(authentication));
  }
}
