package ua.kishkastrybaie.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.kishkastrybaie.repository.entity.User;

public interface UserService extends UserDetailsService {
  Iterable<User> findAll();

  User findById(Long id);

  User create(User user);

  void deleteById(Long id);

  User getCurrentUser();
}
