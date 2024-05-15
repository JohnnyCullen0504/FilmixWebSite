package com.hhu.filmix.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author max
 * @implNote 通用返回类型接口
 *
 */
@Accessors(chain = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(title = "通用返回封装体",description = "定义后端服务整体返回的内容样式")
public class ApiResult<T> implements Serializable {
    @Schema(title = "状态码",description = "具有特定含义的状态码,详见开发说明书",$schema = "200")
    private Integer code;
    @Schema(title = "返回消息",description = "包含调用接口所返回的消息",$schema = "操作成功")
    private String msg;
    @Schema(title = "返回数据")
    private T data;

    public static <T> ApiResult<T> success() {
        return build(ApiCode.SUCCESS, "操作成功", null);
    }

    public static <T> ApiResult<T> success(String msg) {
        return build(ApiCode.SUCCESS, msg, null);
    }

    public static <T> ApiResult<T> fail(ApiCode apiCode, String msg) {
        return build(apiCode, msg, null);
    }

    public static <T> ApiResult<T> fail(ApiCode apiCode, String msg, T data) {
        return build(apiCode, msg, data);
    }

    public static <T> ApiResult<T> data(T data) {
        return build(ApiCode.SUCCESS, "操作成功", data);
    }

    public static <T> ApiResult<T> data(String msg, T data) {
        return build(ApiCode.SUCCESS,msg, data);
    }

    private static <T> ApiResult<T> build(ApiCode code, String msg, T data) {
        ApiResult<T> result = new ApiResult<>();
        result
                .setCode(code.value())
                .setMsg(msg)
                .setData(data);

        return result;
    }

}
