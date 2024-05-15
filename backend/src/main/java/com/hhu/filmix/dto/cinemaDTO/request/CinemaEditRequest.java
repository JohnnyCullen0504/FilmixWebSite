package com.hhu.filmix.dto.cinemaDTO.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaEditRequest {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "影院id")
    @NotNull(message = "影院id不能为null")
    private Long cinemaId;
    @Schema(description = "影院名称")
    private String name;
    @Schema(description = "影院地址")
    private String address;
}
