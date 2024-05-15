package com.hhu.filmix.dto;

import io.swagger.v3.oas.annotations.Parameter;

public record CinemaEditRequestDTO(
        @Parameter()
        String name,
        String title
) {
}
