package ua.kishkastrybaie.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ua.kishkastrybaie.validation.phonenumber.PhoneNumber;

public record UserRequestDto(
    @NotBlank @Size(min = 3) String firstName,
    String middleName,
    @NotBlank @Size(min = 3) String lastName,
    @NotBlank @Email String email,
    @PhoneNumber String phoneNumber) {}
