package com.hhu.filmix.dto.cinemaDTO.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

public record  CinemaDTO(
        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "影院id",type = "string")
        Long cinemaId,
        @Schema(description = "影院名称")
        String name,
        @Schema(description = "影院地址")
        String address
) {
}
