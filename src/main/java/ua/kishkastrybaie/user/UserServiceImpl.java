package ua.kishkastrybaie.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

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
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));

    return new UserDto(
        user.getFirstName(),
        user.getMiddleName(),
        user.getLastName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getRole());
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
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));

    return new UserDto(
        user.getFirstName(),
        user.getMiddleName(),
        user.getLastName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getRole());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmailEqualsIgnoreCase(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
