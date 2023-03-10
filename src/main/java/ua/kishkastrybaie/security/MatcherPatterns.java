package ua.kishkastrybaie.security;

public interface MatcherPatterns {
  String[] PUBLIC = new String[] {"/api/auth/**"};
  String[] PUBLIC_GET =
      new String[] {"/api/products/**", "/api/categories/**", "/api/", "/**"};
}
