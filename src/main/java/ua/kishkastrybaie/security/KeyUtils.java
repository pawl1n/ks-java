package ua.kishkastrybaie.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({KeyPairProperties.class})
public class KeyUtils {
  private final KeyPairProperties keyPairProperties;

  public KeyUtils() {
    KeyPair accessTokenKeypair = KeyGeneratorUtils.generateRsaKey();
    KeyPairProperties.TokenKeyPair accessTokenKeyPair = new KeyPairProperties.TokenKeyPair(
        (RSAPrivateKey) accessTokenKeypair.getPrivate(),
        (RSAPublicKey) accessTokenKeypair.getPublic()
    );

    KeyPair refreshTokenKeypair = KeyGeneratorUtils.generateRsaKey();
    KeyPairProperties.TokenKeyPair refreshTokenKeyPair = new KeyPairProperties.TokenKeyPair(
        (RSAPrivateKey) refreshTokenKeypair.getPrivate(),
        (RSAPublicKey) refreshTokenKeypair.getPublic()
    );
    
    keyPairProperties = new KeyPairProperties(accessTokenKeyPair, refreshTokenKeyPair);
  }

  public RSAPublicKey getAccessTokenPublicKey() {
    return keyPairProperties.getAccessToken().publicKey();
  }

  public RSAPublicKey getRefreshTokenPublicKey() {
    return keyPairProperties.getRefreshToken().publicKey();
  }

  public JWKSource<SecurityContext> getAccessTokenJwkSource() {
    RSAKey rsaKey =
            new RSAKey.Builder(keyPairProperties.getAccessToken().publicKey())
                    .privateKey(keyPairProperties.getAccessToken().privateKey())
                    .build();

    JWKSet jwkSet = new JWKSet(rsaKey);
    return  (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  public JWKSource<SecurityContext> getRefreshTokenJwkSource() {
    RSAKey rsaKey =
            new RSAKey.Builder(keyPairProperties.getRefreshToken().publicKey())
                    .privateKey(keyPairProperties.getRefreshToken().privateKey())
                    .build();

    JWKSet jwkSet = new JWKSet(rsaKey);
    return  (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }
}
