package ua.kishkastrybaie.service.implementation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.service.TokenService;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final JwtEncoder encoder;

  @Override
  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    String scope =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.DAYS))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();

    return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
