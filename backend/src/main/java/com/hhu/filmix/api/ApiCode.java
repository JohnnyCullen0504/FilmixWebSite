package com.hhu.filmix.api;

public enum ApiCode {
    SUCCESS(200,"请求成功"),//请求成功
    AUTHENTICATE_FAILED(1001,"鉴权（登录）失败"),//鉴权（登录）失败
    UNAUTHORIZED(1002,"未登录"),//未登录
    NOT_ALLOWED(1003,"无访问权限"),//无访问权限

    NOTFOUND (2001,"资源不存在"),//资源不存在
    RESOURCE_EXISTS(2002,"资源已存在(重复)"),//资源已存在
    MISSING_ARGUMENT(2003,"缺少请求参数"),//缺少请求参数
    WRONG_ARGUMENT(2004,"参数错误"),//参数错误
    RESOURCE_CONFLICT(2005,"资源冲突"),//资源冲突
    UNABLE(2006,"无法完成该操作"),
    SERVER_ERROR(5001,"服务器内部错误");//服务器内部错误




    private Integer code;
    private String description;
    ApiCode(Integer code,String description){
        this.code = code;
        this.description = description;
    }
    public Integer value(){
        return code;
    }
    public String description(){
        return description;
    }
}
