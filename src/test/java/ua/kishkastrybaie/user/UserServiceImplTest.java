package ua.kishkastrybaie.user;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserModelAssembler userModelAssembler;
  @InjectMocks private UserServiceImpl userService;

  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setFirstName("FirstName");
    user.setMiddleName("MiddleName");
    user.setLastName("LastName");
    user.setEmail("Email");
    user.setPhoneNumber("PhoneNumber");
    user.setPassword("Password");
    user.setRole(Role.USER);

    userDto = new UserDto("FirstName", "MiddleName", "LastName", "Email", "PhoneNumber", Role.USER);
  }

  @Test
  void shouldGetUserDetails() {
    // given
    given(userModelAssembler.toModel(user)).willReturn(userDto);

    // when
    UserDto result = userService.getUserDetails(user);

    // then
    then(result).isEqualTo(userDto);
  }

  @Test
  void shouldUpdate() {
    // given
    UserRequestDto userRequestDto =
        new UserRequestDto(
            "newFirstName", "newMiddleName", "newLastName", "newEmail", "newPhoneNumber");

    given(
            userRepository.save(
                argThat(
                    user ->
                        user.getFirstName().equals("newFirstName")
                            && user.getMiddleName().equals("newMiddleName")
                            && user.getLastName().equals("newLastName")
                            && user.getEmail().equals("newEmail")
                            && user.getPhoneNumber().equals("newPhoneNumber"))))
        .willReturn(user);
    given(userModelAssembler.toModel(user)).willReturn(userDto);

    // when
    UserDto result = userService.update(user, userRequestDto);

    // then
    then(result).isEqualTo(userDto);
  }

  @Test
  void shouldChangePassword() {
    // given
    ChangePasswordRequest changePasswordRequest =
        new ChangePasswordRequest("Password", "newPassword");

    given(passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword()))
        .willReturn(true);
    given(passwordEncoder.encode(changePasswordRequest.newPassword()))
        .willReturn("encodedNewPassword");
    given(userRepository.save(argThat(user -> user.getPassword().equals("encodedNewPassword"))))
        .willReturn(user);
    given(userModelAssembler.toModel(user)).willReturn(userDto);

    // when
    UserDto result = userService.changePassword(user, changePasswordRequest);

    // then
    then(result).isEqualTo(userDto);
  }

  @Test
  void shouldNotChangePasswordWhenSame() {
    // given

    // when
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password", "Password");

    // then
    thenThrownBy(() -> userService.changePassword(user, changePasswordRequest))
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage("Current password and new password must be different");
  }

  @Test
  void shouldNotChangePasswordWhenInvalidCurrentPassword() {
    // given
    ChangePasswordRequest changePasswordRequest =
        new ChangePasswordRequest("invalidPassword", "Password");

    // when
    when(passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword()))
        .thenReturn(false);

    // then
    thenThrownBy(() -> userService.changePassword(user, changePasswordRequest))
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage("Bad credentials");
  }

  @Test
  void shouldLoadUserByUsername() {
    // given
    String username = "userEmail";

    // when
    when(userRepository.findByEmailEqualsIgnoreCase(username)).thenReturn(Optional.of(user));

    // then
    then(userService.loadUserByUsername(username)).isEqualTo(user);
  }

  @Test
  void shouldNotLoadUserByUsernameWhenInvalidUsername() {
    // given
    String username = "invalidUsername";

    // when
    when(userRepository.findByEmailEqualsIgnoreCase(username)).thenReturn(Optional.empty());

    // then
    thenThrownBy(() -> userService.loadUserByUsername(username))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessage("User not found");
  }
}
