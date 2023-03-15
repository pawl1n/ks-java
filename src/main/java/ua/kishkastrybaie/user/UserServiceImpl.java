package ua.kishkastrybaie.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserModelAssembler userModelAssembler;

  @Override
  public UserDto getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }

    String email = authentication.getName();

    User user =
        userRepository
            .findByEmailEqualsIgnoreCase(email)
            .orElseThrow(() -> new EmailNotFoundException(email));

    return userModelAssembler.toModel(user);
  }

  @Override
  public UserDto update(UserRequestDto userRequestDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }

    String email = authentication.getName();

    User user =
        userRepository
            .findByEmailEqualsIgnoreCase(email)
            .map(
                u -> {
                  u.setFirstName(userRequestDto.firstName());
                  u.setMiddleName(userRequestDto.middleName());
                  u.setLastName(userRequestDto.lastName());
                  u.setEmail(userRequestDto.email());
                  u.setPhoneNumber(userRequestDto.phoneNumber());

                  return userRepository.save(u);
                })
            .orElseThrow(() -> new EmailNotFoundException(email));

    return userModelAssembler.toModel(user);
  }

  @Override
  public UserDto changePassword(ChangePasswordRequest changePasswordRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }

    if (changePasswordRequest.currentPassword().equals(changePasswordRequest.newPassword())) {
      throw new BadCredentialsException("Current password and new password must be different");
    }

    String email = authentication.getName();

    User user =
        userRepository
            .findByEmailEqualsIgnoreCase(email)
            .orElseThrow(() -> new EmailNotFoundException(email));

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
