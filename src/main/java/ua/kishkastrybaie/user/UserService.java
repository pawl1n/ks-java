package ua.kishkastrybaie.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  UserDto getUserDetails(User user);

  UserDto update(User user, UserRequestDto userRequestDto);

  UserDto changePassword(User user, ChangePasswordRequest changePasswordRequest);
}
