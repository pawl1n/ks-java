package ua.kishkastrybaie.shared;

import ua.kishkastrybaie.user.User;

public interface AuthorizationService {
  boolean isAdmin();

  User getAuthenticatedUser();
}
