package ua.kishkastrybaie.authentication;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.user.User;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  @Resource private final JwtEncoder jwtAccessTokenEncoder;
  @Resource private final JwtEncoder jwtRefreshTokenEncoder;

  private String generateAccessToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.MINUTES))
            .subject(authentication.getName())
            .build();

    return jwtAccessTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private String generateRefreshToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.DAYS))
            .subject(authentication.getName())
            .build();

    return jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  @Override
  public TokenDto generateToken(Authentication authentication) {
    if (!(authentication.getPrincipal() instanceof User)) {
      throw new IllegalArgumentException("Principal must be instance of User");
    }

    String accessToken = generateAccessToken(authentication);
    String refreshToken;
    if (authentication.getCredentials() instanceof Jwt jwt
        && Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() > 7) {
      refreshToken = jwt.getTokenValue();
    } else {
      refreshToken = generateRefreshToken(authentication);
    }
    return new TokenDto(accessToken, refreshToken);
  }
}
