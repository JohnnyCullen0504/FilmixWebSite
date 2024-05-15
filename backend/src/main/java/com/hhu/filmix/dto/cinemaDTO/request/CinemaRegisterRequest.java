package com.hhu.filmix.dto.cinemaDTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CinemaRegisterRequest {
    @Schema(description = "影院名称")
    @NotNull(message = "名称不能为空")
    @NotBlank(message = "名称不能为空" )
    private String name;

    @Schema(description = "影院地址")
    @NotNull(message = "地址不能为空")
    @NotBlank(message = "地址不能为空" )
    private String address;
}
