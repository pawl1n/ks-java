package ua.kishkastrybaie.authentication;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

  @Mock UserRepository userRepository;
  @Mock PasswordEncoder passwordEncoder;
  @Mock TokenService tokenService;
  @Mock AuthenticationManager authenticationManager;
  @Mock Authentication authentication;
  @InjectMocks AuthenticationServiceImpl authenticationService;

  @Test
  void shouldRegister() {
    // given
    RegisterRequest registerRequest =
        new RegisterRequest(
            "firstName", "middleName", "lastName", "email@email.com", "phoneNumber", "password");

    User user =
        new User(
            null,
            "firstName",
            "middleName",
            "lastName",
            "email@email.com",
            "phoneNumber",
            "password",
            Role.USER);

    given(passwordEncoder.encode("password")).willReturn("password");
    given(userRepository.save(user)).willReturn(user);
    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(tokenService.generateToken(authentication)).willReturn("token");

    // when
    AuthenticationResponse response = authenticationService.register(registerRequest);

    // then
    then(response).isNotNull().isEqualTo(new AuthenticationResponse("token"));
    verify(passwordEncoder).encode("password");
    verify(userRepository).save(user);
    verify(authenticationManager)
        .authenticate(new UsernamePasswordAuthenticationToken("email@email.com", "password"));
    verify(tokenService).generateToken(authentication);
  }

  @Test
  void shouldAuthenticate() {
    // given
    AuthenticationRequest authenticationRequest =
        new AuthenticationRequest("email@email.com", "password");

    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(tokenService.generateToken(authentication)).willReturn("token");

    // when
    AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

    // then
    then(response).isNotNull().isEqualTo(new AuthenticationResponse("token"));
    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(tokenService).generateToken(authentication);
  }
}
