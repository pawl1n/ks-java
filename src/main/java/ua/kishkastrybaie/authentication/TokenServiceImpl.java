package ua.kishkastrybaie.authentication;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.user.User;

@Service
public class TokenServiceImpl implements TokenService {
  private final JwtEncoder accessTokenEncoder;
  private final JwtEncoder refreshTokenEncoder;

  public TokenServiceImpl(
      JwtEncoder accessTokenEncoder, @Qualifier("refreshToken") JwtEncoder refreshTokenEncoder) {
    this.accessTokenEncoder = accessTokenEncoder;
    this.refreshTokenEncoder = refreshTokenEncoder;
  }

  private String generateAccessToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.MINUTES))
            .subject(authentication.getName())
            .build();

    return accessTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
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

    return refreshTokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
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
