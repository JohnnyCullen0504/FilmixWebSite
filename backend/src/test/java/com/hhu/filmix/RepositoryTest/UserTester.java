package com.hhu.filmix.RepositoryTest;

import cn.hutool.core.lang.Assert;
import com.hhu.filmix.FilmixBackendApplication;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.userDTO.request.UserRegisterRequest;
import com.hhu.filmix.entity.User;
import com.hhu.filmix.enumeration.UserType;
import com.hhu.filmix.repository.UserRepository;
import com.hhu.filmix.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {FilmixBackendApplication.class})
@Slf4j
public class UserTester {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepository;

    @Test
    public void GenerateUser(){
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserName("max");
        request.setEmail("270401247@qq.com");
        request.setUserType(UserType.USER);
        request.setPassword("password");
        ApiResult result = userService.register(request);
        logger.debug(result.toString());
        System.out.println(result);
        User user = userRepository.findUserByEmail(request.getEmail()).orElse(null);
        Assert.equals(request.getEmail(),user.getEmail());
    }
    @Test void userPersistentAndDelete(){
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserName("user-test");
        request.setEmail("user@filmix.com");
        request.setUserType(UserType.USER);
        request.setPassword("password");
        ApiResult result = userService.register(request);
        logger.debug(result.toString());
        System.out.println(result);
        User user = userRepository.findUserByEmail(request.getEmail()).orElse(null);

        Assert.equals(request.getEmail(),user.getEmail());
       //TODO:DeleteTest
    }

}
