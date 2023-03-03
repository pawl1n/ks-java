package ua.kishkastrybaie.controller.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {
}
