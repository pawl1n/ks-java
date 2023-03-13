package ua.kishkastrybaie.user;

public record UserDto(
    String firstName,
    String middleName,
    String lastName,
    String email,
    String phoneNumber,
    Role role) {}
