package ua.kishkastrybaie.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Size(min = 3) String firstName,
    String middleName,
    String lastName,
    @NotBlank @Email String email,
    @Pattern(regexp = "^(\\+?38)?(0[0-9]{2})[0-9]{7}$") String phoneNumber,
    @Size(min = 7) String password) {}
