package com.hhu.filmix.dto.userDTO.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhu.filmix.enumeration.UserType;
import io.swagger.v3.oas.annotations.media.Schema;


public record UserDTO (
        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "用户id")
        Long id,
        @Schema(description = "用户名称")
        String name,
        @Schema(description = "邮件")
        String email,
        @Schema(title = "用户类型")
        UserType type
){}
