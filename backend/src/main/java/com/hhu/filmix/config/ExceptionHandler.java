package com.hhu.filmix.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Configuration
public class ExceptionHandler {
    /**
     * 用户登录异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({NotLoginException.class})
    public ApiResult<?> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.UNAUTHORIZED, "未登录，或Token无效");
    }

    /**
     * 用户权限异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({NotPermissionException.class})
    public ApiResult<?> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.NOT_ALLOWED, "当前用户无此操作权限");
    }

    /**
     * 用户身份异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({NotRoleException.class})
    public ApiResult<?> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.NOT_ALLOWED, "当前用户无此操作权限");
    }
    /**
     * 参数异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class, DateTimeParseException.class, MethodArgumentTypeMismatchException.class})
    public ApiResult<?> handleIllegalArgumentException(Exception e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.WRONG_ARGUMENT, "传入参数有误，请检查参数："+e.getMessage());
    }
    /**
     * 枚举类型异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({ HttpMessageNotReadableException.class})
    public ApiResult<?> handInvalidFormatException(HttpMessageNotReadableException e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.WRONG_ARGUMENT, "参数转换异常："+e.getCause().getMessage());
    }

    /**
     *参数校验异常
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResult<?> handleIllegalMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        //logger.logError(e, request);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));
        return ApiResult.fail(ApiCode.WRONG_ARGUMENT, "参数校验异常："+message+";");
    }
    /**
     *404 NOT FOUND
     */
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({NoHandlerFoundException.class})
    public ApiResult<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        //logger.logError(e, request);
        return ApiResult.fail(ApiCode.NOTFOUND, "该接口不存在");
    }
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ApiResult<?> handleException(Exception e, HttpServletRequest request) {
        //logger.logError(e, request);
        // 打印堆栈，方便溯源
        e.printStackTrace();
        return ApiResult.fail(ApiCode.SERVER_ERROR, "发生错误，请稍后再试:"+e.getMessage());
    }
}
