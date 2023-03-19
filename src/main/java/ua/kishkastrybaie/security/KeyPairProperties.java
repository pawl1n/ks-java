package ua.kishkastrybaie.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
public class KeyPairProperties {
  private final TokenKeyPair accessToken;
  private final TokenKeyPair refreshToken;

  public record TokenKeyPair(RSAPrivateKey privateKey, RSAPublicKey publicKey) {}
}
