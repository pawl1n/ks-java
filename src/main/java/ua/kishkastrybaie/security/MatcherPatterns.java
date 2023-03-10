package ua.kishkastrybaie.security;

public interface MatcherPatterns {
  String[] PUBLIC = new String[] {"/api/v1/auth/**"};
  String[] PUBLIC_GET =
      new String[] {"/api/v1/products/**", "/api/v1/categories/**", "/api/v1/", "/**"};
}
