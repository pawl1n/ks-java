package ua.kishkastrybaie.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kishkastrybaie.exception.UserNotFoundException;
import ua.kishkastrybaie.repository.UserRepository;
import ua.kishkastrybaie.repository.entity.User;
import ua.kishkastrybaie.service.UserService;

// @Service // ignore for now
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Iterable<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  @Override
  public User create(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmailEqualsIgnoreCase(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public User getCurrentUser() {
    return userRepository
        .findByEmailEqualsIgnoreCase(
            SecurityContextHolder.getContext().getAuthentication().getName())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
