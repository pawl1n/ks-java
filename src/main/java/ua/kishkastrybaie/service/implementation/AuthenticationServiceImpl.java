package ua.kishkastrybaie.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.controller.dto.AuthenticationRequest;
import ua.kishkastrybaie.controller.dto.AuthenticationResponse;
import ua.kishkastrybaie.controller.dto.RegisterRequest;
import ua.kishkastrybaie.repository.UserRepository;
import ua.kishkastrybaie.repository.entity.Role;
import ua.kishkastrybaie.repository.entity.User;
import ua.kishkastrybaie.service.AuthenticationService;
import ua.kishkastrybaie.service.TokenService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
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

        userRepository.save(userDetails);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return authenticationResponse(authentication);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return authenticationResponse(authentication);
    }

    private AuthenticationResponse authenticationResponse(Authentication authentication) {
        return AuthenticationResponse.builder()
                .token(tokenService.generateToken(authentication))
                .build();
    }
}
