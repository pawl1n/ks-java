package ua.kishkastrybaie.user;

public class BadCredentialsException extends RuntimeException {
  public BadCredentialsException() {
    super("Bad credentials");
  }

  public BadCredentialsException(String message) {
    super(message);
  }
}
