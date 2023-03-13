package ua.kishkastrybaie.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  UserDto getCurrentUser();

  UserDto update(UserRequestDto userRequestDto);
}
