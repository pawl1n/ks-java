package ua.kishkastrybaie.authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  public AuthenticationServiceImpl(
      UserRepository userRepository,
      TokenService tokenService,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder,
      @Qualifier("refreshToken") JwtAuthenticationProvider jwtAuthenticationProvider) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
  }

  @Override
  public TokenDto register(RegisterRequest request) {
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

    return tokenService.generateToken(authentication);
  }

  @Override
  public TokenDto authenticate(AuthenticationRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    return tokenService.generateToken(authentication);
  }

  @Override
  public TokenDto refresh(RefreshRequest request) {
    Authentication authentication =
        jwtAuthenticationProvider.authenticate(
            new BearerTokenAuthenticationToken(request.refreshToken()));

    return tokenService.generateToken(authentication);
  }
}
