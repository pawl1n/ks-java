package ua.kishkastrybaie.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ua.kishkastrybaie.user.User;
import ua.kishkastrybaie.user.UserRepository;

@Component
@RequiredArgsConstructor
public class JwtToUserConverter implements Converter<Jwt,UsernamePasswordAuthenticationToken> {
    private final UserRepository userRepository;

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
    User user =
        userRepository
            .findByEmailEqualsIgnoreCase(jwt.getSubject())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
  }
}
