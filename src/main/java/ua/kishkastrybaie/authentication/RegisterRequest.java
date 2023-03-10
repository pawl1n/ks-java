package ua.kishkastrybaie.authentication;

public record RegisterRequest(
    String firstName,
    String middleName,
    String lastName,
    String email,
    String phoneNumber,
    String password) {}
