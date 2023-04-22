package ua.kishkastrybaie.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.kishkastrybaie.user.Role;
import ua.kishkastrybaie.user.User;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
  @Override
  public boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }
    return authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.withPrefix()));
  }

  @Override
  public User getAuthenticatedUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof User user) {
      return user;
    } else {
      throw new UsernameNotFoundException(principal.toString());
    }
  }

  @Override
  public boolean isAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }
}
