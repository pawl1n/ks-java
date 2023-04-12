package ua.kishkastrybaie.user;

public enum Role {
  USER,
  ADMIN;

  public String withPrefix() {
    return "ROLE_" + name().toUpperCase();
  }
}
