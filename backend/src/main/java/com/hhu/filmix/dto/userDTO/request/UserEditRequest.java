package com.hhu.filmix.dto.userDTO.request;

import com.hhu.filmix.enumeration.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEditRequest {
    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "新用户名")
    private String userName;
    @Schema(description = "新电子邮件")
    private String phone;
    @Schema(description = "新电子邮件")
    private String email;
    @Schema(description = "原密码")
    private String oldPassword;
    @Schema(title = "新密码",description = "修改密码时需要提供原密码")
    private String newPassword;
    @Schema(description = "新用户类型")
    private UserType userType;
}
