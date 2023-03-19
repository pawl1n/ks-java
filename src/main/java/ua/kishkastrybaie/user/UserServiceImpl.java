package ua.kishkastrybaie.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.shared.AuthorizationService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserModelAssembler userModelAssembler;
  private final AuthorizationService authorizationService;

  @Override
  public UserDto getCurrentUser() {
    return userModelAssembler.toModel(authorizationService.getAuthenticatedUser());
  }

  @Override
  public UserDto update(UserRequestDto userRequestDto) {
    User user = authorizationService.getAuthenticatedUser();

    user.setFirstName(userRequestDto.firstName());
    user.setMiddleName(userRequestDto.middleName());
    user.setLastName(userRequestDto.lastName());
    user.setEmail(userRequestDto.email());
    user.setPhoneNumber(userRequestDto.phoneNumber());

    userRepository.save(user);
    return userModelAssembler.toModel(user);
  }

  @Override
  public UserDto changePassword(ChangePasswordRequest changePasswordRequest) {
    if (changePasswordRequest.currentPassword().equals(changePasswordRequest.newPassword())) {
      throw new BadCredentialsException("Current password and new password must be different");
    }

    User user = authorizationService.getAuthenticatedUser();

    if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword())) {
      throw new BadCredentialsException();
    }

    user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));

    return userModelAssembler.toModel(userRepository.save(user));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmailEqualsIgnoreCase(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
