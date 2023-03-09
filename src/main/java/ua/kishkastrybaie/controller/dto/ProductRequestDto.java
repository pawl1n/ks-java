package ua.kishkastrybaie.controller.dto;

import java.net.URL;

public record ProductRequestDto(String name, String description, Long category, URL mainImage) {}
