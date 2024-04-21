package ua.kishkastrybaie.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            String password = RandomStringUtils.randomAlphanumeric(10);
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("Admin");
            adminUser.setEmail("admin@localhost");
            adminUser.setPassword(passwordEncoder.encode(password));
            adminUser.setPhoneNumber("123456789");
            adminUser.setRole(Role.ADMIN);

            userRepository.save(adminUser);

            log.info("Admin user created: email={}, password={}", adminUser.getEmail(), password);
        }
    }
}

