package ua.kishkastrybaie.controller.dto;

public record RegisterRequest(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        String password
) {
}
