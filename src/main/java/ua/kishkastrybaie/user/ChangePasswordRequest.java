package ua.kishkastrybaie.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
    String currentPassword, @NotBlank @Size(min = 7) String newPassword) {}
