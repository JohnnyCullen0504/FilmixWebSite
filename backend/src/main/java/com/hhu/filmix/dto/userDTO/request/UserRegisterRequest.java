package com.hhu.filmix.dto.userDTO.request;

import com.hhu.filmix.enumeration.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @Schema(description = "用于登录的电子邮件")
    @NotNull(message = "邮件不能为空")
    @NotBlank(message = "邮件不能为空")
    private String email;

    @Schema(description = "用户手机号")
    @NotNull(message = "手机号不能为空")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Schema(description = "登录密码")
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "用户类型")
    @NotNull(message = "类型不能为空")
    private UserType userType;
}
