package ua.kishkastrybaie.user;

import lombok.RequiredArgsConstructor;
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
  public UserDto getUserDetails(User user) {

    return userModelAssembler.toModel(user);
  }

  @Override
  public UserDto update(User user, UserRequestDto userRequestDto) {
    user.setFirstName(userRequestDto.firstName());
    user.setMiddleName(userRequestDto.middleName());
    user.setLastName(userRequestDto.lastName());
    user.setEmail(userRequestDto.email());
    user.setPhoneNumber(userRequestDto.phoneNumber());

    userRepository.save(user);
    return userModelAssembler.toModel(user);
  }

  @Override
  public UserDto changePassword(User user, ChangePasswordRequest changePasswordRequest) {
    if (changePasswordRequest.currentPassword().equals(changePasswordRequest.newPassword())) {
      throw new BadCredentialsException("Current password and new password must be different");
    }

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
