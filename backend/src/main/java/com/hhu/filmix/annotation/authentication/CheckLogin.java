package com.hhu.filmix.annotation.authentication;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SaCheckLogin()
@SecurityRequirement(name = "authToken")
@Parameter(name = "authToken",description = "用户的jwt令牌", required = true,in = ParameterIn.HEADER)
public @interface CheckLogin {
}
