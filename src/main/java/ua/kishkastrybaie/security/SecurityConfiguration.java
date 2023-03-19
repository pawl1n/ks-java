package ua.kishkastrybaie.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.kishkastrybaie.user.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private static final String[] PUBLIC = new String[] {"/api/auth/**"};
  private static final String[] PUBLIC_GET =
      new String[] {"/api/products/**", "/api/categories/**", "/api/", "/**"};
  private final KeyUtils keyUtils;
  private final JwtToUserConverter jwtToUserConverter;

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return new ProviderManager(authProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(PUBLIC)
                    .permitAll()
                    .requestMatchers(GET, PUBLIC_GET)
                    .permitAll()
                    .anyRequest()
                    .hasRole(Role.ADMIN.name()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(configurer -> configurer.jwt().jwtAuthenticationConverter(jwtToUserConverter))
        .build();
  }

  @Bean
  RoleHierarchy roleHierarchy() {
    Map<String, List<String>> roleHierarchyMap = new HashMap<>();
    roleHierarchyMap.put(Role.ADMIN.name(), List.of(Role.USER.name()));
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap));
    return roleHierarchy;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3030"));
    configuration.setAllowedMethods(List.of(GET.name(), POST.name(), PUT.name(), DELETE.name()));
    configuration.setAllowedHeaders(List.of(AUTHORIZATION, CONTENT_TYPE));
    configuration.addExposedHeader("WWW-Authenticate");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }


  @Bean
  @Primary
  JwtEncoder jwtAccessTokenEncoder() {
    return new NimbusJwtEncoder(keyUtils.getAccessTokenJwkSource());
  }

  @Bean
  @Primary
  JwtDecoder jwtAccessTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getAccessTokenPublicKey()).build();
  }

  @Bean
  @Qualifier("refreshToken")
  JwtEncoder jwtRefreshTokenEncoder() {
    return new NimbusJwtEncoder(keyUtils.getRefreshTokenJwkSource());
  }

  @Bean
  @Qualifier("refreshToken")
  JwtDecoder jwtRefreshTokenDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyUtils.getRefreshTokenPublicKey()).build();
  }

  @Bean
  @Qualifier("refreshToken")
  JwtAuthenticationProvider jwtAuthenticationProvider() {
    JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
    jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtToUserConverter);
    return jwtAuthenticationProvider;
  }
}
