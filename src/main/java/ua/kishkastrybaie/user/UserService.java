package ua.kishkastrybaie.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.kishkastrybaie.shared.StatisticsDto;

import java.time.Instant;

public interface UserService extends UserDetailsService {
  UserDto getUserDetails(User user);

  UserDto update(User user, UserRequestDto userRequestDto);

  UserDto changePassword(User user, ChangePasswordRequest changePasswordRequest);

  StatisticsDto getCountStatistics(Role role, Instant startDate, Instant endDate);
}
