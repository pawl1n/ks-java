package ua.kishkastrybaie.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminUserInitializerTest {
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private AdminUserInitializer adminUserInitializer;

  @Test
  void shouldAddAdmin() {
    // given
    given(userRepository.count()).willReturn(0L);
    given(passwordEncoder.encode(any())).willReturn("encodedPassword");

    // when
    adminUserInitializer.run();

    // then
    then(userRepository)
        .should()
        .save(
            argThat(
                user ->
                    user.getRole() == Role.ADMIN
                        && user.getFirstName().equals("Admin")
                        && user.getEmail().equals("admin@admin")));
  }

  @Test
  void shouldNotAddAdminWhenUserExists() {
    // given
    given(userRepository.count()).willReturn(1L);

    // when
    adminUserInitializer.run();

    // then
    then(userRepository).shouldHaveNoMoreInteractions();
  }
}
