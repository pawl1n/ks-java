package ua.kishkastrybaie.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimType);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, ?> extraClaims, UserDetails userDetails);
    boolean validateToken(String token);
}
