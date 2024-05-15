package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.userDTO.request.UserEditRequest;
import com.hhu.filmix.dto.userDTO.request.UserRegisterRequest;
import com.hhu.filmix.dto.userDTO.response.LoginResponse;
import com.hhu.filmix.dto.userDTO.response.UserDTO;

public interface UserService {
    public ApiResult<LoginResponse> login(String email, String password);

    public ApiResult<?> logout();
    public ApiResult<UserDTO> getUserInfo(Long userId, Boolean advQuery);
    public ApiResult<?> register(UserRegisterRequest request);

    ApiResult<?> editUser(UserEditRequest request,Boolean advEdit);
}
