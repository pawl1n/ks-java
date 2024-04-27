package ua.kishkastrybaie.authentication;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
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
  @Mock JwtAuthenticationProvider jwtAuthenticationProvider;
  @InjectMocks AuthenticationServiceImpl authenticationService;

  @Test
  void shouldRegister() {
    // given
    RegisterRequest registerRequest =
        new RegisterRequest(
            "firstName", "middleName", "lastName", "userEmail@userEmail.com", "phoneNumber", "password");

    User user =
        new User(
            null,
            "firstName",
            "middleName",
            "lastName",
            "userEmail@userEmail.com",
            "phoneNumber",
            "password",
            Role.USER);

    given(passwordEncoder.encode("password")).willReturn("password");
    given(userRepository.save(ArgumentMatchers.any())).willReturn(user);
    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(tokenService.generateToken(authentication))
        .willReturn(new TokenDto("accessToken", "refreshToken"));

    // when
    TokenDto response = authenticationService.register(registerRequest);

    // then
    then(response).isNotNull().isEqualTo(new TokenDto("accessToken", "refreshToken"));
  }

  @Test
  void shouldAuthenticate() {
    // given
    AuthenticationRequest authenticationRequest =
        new AuthenticationRequest("userEmail@userEmail.com", "password");

    given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .willReturn(authentication);
    given(tokenService.generateToken(authentication))
        .willReturn(new TokenDto("accessToken", "refreshToken"));

    // when
    TokenDto response = authenticationService.authenticate(authenticationRequest);

    // then
    then(response).isNotNull().isEqualTo(new TokenDto("accessToken", "refreshToken"));
  }

  @Test
  void shouldRefresh() {
    // given
    RefreshRequest refreshRequest = new RefreshRequest("refreshToken");

    given(jwtAuthenticationProvider.authenticate(any(BearerTokenAuthenticationToken.class))).willReturn(authentication);
    given(tokenService.generateToken(authentication)).willReturn(new TokenDto("accessToken", "refreshToken"));

    // when
    TokenDto response = authenticationService.refresh(refreshRequest);

    // then
    then(response).isNotNull().isEqualTo(new TokenDto("accessToken", "refreshToken"));
  }
}
