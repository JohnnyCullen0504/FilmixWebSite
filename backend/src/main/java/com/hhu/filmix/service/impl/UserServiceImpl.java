package com.hhu.filmix.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.userDTO.request.UserEditRequest;
import com.hhu.filmix.dto.userDTO.request.UserRegisterRequest;
import com.hhu.filmix.dto.userDTO.response.LoginResponse;
import com.hhu.filmix.dto.userDTO.response.UserDTO;
import com.hhu.filmix.dtoMapper.UserDTOMapper;
import com.hhu.filmix.entity.User;
import com.hhu.filmix.repository.UserRepository;
import com.hhu.filmix.service.UserService;
import com.hhu.filmix.util.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private UserDTOMapper userDTOMapper;
    @Override
    public ApiResult<LoginResponse> login(String email, String password) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user == null){
            return ApiResult.fail(ApiCode.AUTHENTICATE_FAILED,"该用户不存在");
        }
        if(bCryptPasswordEncoder.check(password, user.getPassword())){
            StpUtil.login(user.getId().toString(),
                    SaLoginConfig
                            .setExtra("id",user.getId().toString())
                            .setExtra("email",user.getEmail())
                            .setExtra("phone",user.getPhone())
                            .setExtra("userName",user.getName())
                            .setExtra("userType",user.getType().type())
                            .setExtra("userType_ZH",user.getType().ZHType()));
            StpUtil.getTokenValue();
            return ApiResult.data("登录成功",new LoginResponse(StpUtil.getTokenValue()));
        }else {
            //登录失败
            return ApiResult.fail(ApiCode.AUTHENTICATE_FAILED,"用户名或密码错误");
        }
    }

    @Override
    public ApiResult<?> logout() {
        //Controller侧已有鉴权检测，无需重复检测是否登录
        StpUtil.logout();
        return ApiResult.success("退出登录成功");
    }

    @Override
    public ApiResult<UserDTO> getUserInfo(Long userId, Boolean advQuery) {
        User user = userRepository.findUserById(userId).orElse(null);
        if (user == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该用户不存在");
        }
        if( (!user.getId().equals(Long.valueOf(StpUtil.getLoginId().toString()))) &&(!advQuery)){
            return ApiResult.fail(ApiCode.NOT_ALLOWED,"该用户无查询他人信息的权限");
        }

        return ApiResult.data("获取用户信息成功",userDTOMapper.map(user));
    }
    @Override
    public ApiResult<?> register(UserRegisterRequest request) {
        if(request.getEmail() == null|| request.getEmail().isEmpty()){
            return ApiResult.fail(ApiCode.WRONG_ARGUMENT,"非法邮箱,邮箱不能为空");
        }
        //邮箱校验
        if(!Validator.isEmail(request.getEmail())){
            return ApiResult.fail(ApiCode.WRONG_ARGUMENT,"非法邮箱:"+request.getEmail());
        }
        //手机号校验
        if(!Validator.isMobile(request.getPhone())){
            return ApiResult.fail(ApiCode.WRONG_ARGUMENT,"手机号不正确:"+request.getEmail());
        }
        //判断是否已注册
        if(userRepository.findUserByEmail(request.getEmail()).isPresent()){
            return ApiResult.fail(ApiCode.RESOURCE_EXISTS,"此邮箱已被注册");
        }
        User newUser = new User(null,
                request.getUserName(),
                request.getEmail(),
                request.getPhone(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                request.getUserType());
        Long id = userRepository.insertUser(newUser);

        return ApiResult.data("用户注册成功", PutResponseDTO.phrase(id));
    }

    @Override
    public ApiResult<?> editUser(UserEditRequest request,Boolean advEdit) {
        User user = userRepository.findUserById(request.getUserId()).orElse(null);
        if (user == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"当前用户不存在");
        }
        //修改用户信息
        //姓名
        if(request.getUserName()!=null){
            user.setName(request.getUserName());
        }
        //密码
        if(request.getNewPassword()!=null){
            if(bCryptPasswordEncoder.check(request.getOldPassword(), user.getPassword())){
                user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
            }else {
                return ApiResult.fail(ApiCode.AUTHENTICATE_FAILED,"原密码错误");
            }
        }
        //手机号
        if(request.getPhone() != null){
            if(Validator.isMobile(request.getPhone())){
                user.setPhone(request.getPhone());
            }else {
                return ApiResult.fail(ApiCode.WRONG_ARGUMENT,"手机号不正确");
            }

        }        //类型
        if(request.getUserType()!=null && advEdit){
            user.setType(request.getUserType());
        }else if (request.getUserType()!=null && !advEdit){
            return ApiResult.fail(ApiCode.NOT_ALLOWED,"普通用户无权修改该信息");
        }

        userRepository.updateUser(user);
        return ApiResult.success("用户信息修改成功");
    }

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserRepository userRepository,
                           UserDTOMapper userDTOMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }
}
