package com.yl.common.config;

import lombok.Data;

@Data
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    private String traceId;

    // 成功
    public static <T> Result<T> success(int code, String msg, T t) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(t);
        return result;
    }

    // 失败
    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
