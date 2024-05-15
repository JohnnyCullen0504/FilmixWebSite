package com.hhu.filmix.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.userDTO.request.UserEditRequest;
import com.hhu.filmix.dto.userDTO.request.UserRegisterRequest;
import com.hhu.filmix.dto.userDTO.response.LoginResponse;
import com.hhu.filmix.dto.userDTO.response.UserDTO;
import com.hhu.filmix.enumeration.UserType;
import com.hhu.filmix.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "用户账号Api")
public class UserController {
    private UserService userService;
    @PostMapping("/login")
    @Operation( summary= "用户登录",description = "登录接口")
    public ApiResult<LoginResponse> login(@Parameter(description = "用户邮箱")@RequestParam("email") String email,
                                          @Parameter(description = "登录密码")@RequestParam("password") String password){
        return userService.login(email,password);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户注销",description = "退出登录")
    @CheckLogin()
    public ApiResult<?> logout(){
        return userService.logout();
    }

    @PutMapping("/user")
    @Operation(summary = "用户注册")
    public ApiResult<?> register(@Valid @RequestBody UserRegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/user")
    @CheckLogin
    @Operation(summary = "修改用户信息",description = "非管理员和门店经理的账号可修改的用户信息会有限制")
    public ApiResult<?> editUser(@Valid @RequestBody UserEditRequest request) {
        //用户修改自身数据(非高级修改模式)
        if(StpUtil.getLoginId().equals(request.getUserId())
         &&!(StpUtil.getRoleList().contains(UserType.ADMIN.type())))
            return userService.editUser(request,false);

        //ADMIN及GM拥有修改他人信息权限(高级修改模式)
        StpUtil.checkRoleOr(UserType.ADMIN.type());
        return userService.editUser(request,true);
    }
    @GetMapping("user/{id}/info")
    @CheckLogin
    @Operation(summary = "获取用户信息")
    @Parameter(name = "authToken",description = "用户的jwt令牌", required = true,in = ParameterIn.HEADER)
    public ApiResult<UserDTO> userInfo(@Parameter(description = "用户编号")@PathVariable("id") Long userId){
        Boolean advQuery =true;
        //检测是否有权限查看他人信息
        if(StpUtil.getRoleList().contains(UserType.ADMIN.type()))
            advQuery = false;
        return userService.getUserInfo(userId,advQuery);
    }


    public UserController(UserService userService) {
        this.userService = userService;
    }
}
