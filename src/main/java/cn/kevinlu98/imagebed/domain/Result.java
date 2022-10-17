package cn.kevinlu98.imagebed.domain;

import cn.kevinlu98.imagebed.domain.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 4:39 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), null);
    }

    public static <T> Result<T> success(T data) {
        return success(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> success(String msg) {
        return success(ResponseCode.SUCCESS.getCode(), msg, null);
    }


    public static <T> Result<T> success(int code, String msg) {
        return buildResponse(code, msg, null);
    }

    public static <T> Result<T> success(int code, String msg, T data) {
        return buildResponse(code, msg, data);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return buildResponse(code, msg, null);
    }

    public static <T> Result<T> fail(ResponseCode code) {
        return buildResponse(code.getCode(), code.getMsg(), null);
    }

    public static <T> Result<T> fail(int code, String msg, T data) {
        return buildResponse(code, msg, data);
    }


    public static <T> Result<T> buildResponse(int code, String msg, T data) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }
}
