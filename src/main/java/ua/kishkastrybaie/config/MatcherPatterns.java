package ua.kishkastrybaie.config;

public interface MatcherPatterns {
    String[] PUBLIC = new String[] {
            "/api/v1/auth/**"
    };
    String[] PUBLIC_GET = new String[] {
            "/api/v1/products/**",
            "/api/v1/categories/**"
    };
}
