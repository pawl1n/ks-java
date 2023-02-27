package ua.kishkastrybaie.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.controller.dto.AuthenticationRequest;
import ua.kishkastrybaie.controller.dto.AuthenticationResponse;
import ua.kishkastrybaie.controller.dto.RegisterRequest;
import ua.kishkastrybaie.repository.UserRepository;
import ua.kishkastrybaie.repository.entity.Role;
import ua.kishkastrybaie.repository.entity.User;
import ua.kishkastrybaie.service.AuthenticationService;
import ua.kishkastrybaie.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User userDetails = User.builder()
                .firstName(request.firstName())
                .middleName(request.middleName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(Role.USER)
                .build();

        User user = userRepository.save(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmailEqualsIgnoreCase(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .authorities(
                        user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new)
                )
                .build();
    }
}
