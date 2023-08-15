package com.yl.common.config;

public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "系统错误"),
    FAILED(101, "操作失败"),
    UNAUTHORIZED(102, "登录超时"),
    PARAM_ERROR(103, "参数错误"),
    INVALID_PARAM_EXIST(104, "请求参数已存在"),
    INVALID_PARAM_EMPTY(105, "请求参数为空"),
    PARAM_TYPE_MISMATCH(106, "参数类型不匹配"),
    PARAM_VALID_ERROR(107, "参数校验失败"),
    ILLEGAL_REQUEST(108, "非法请求"),
    INVALID_VCODE(204, "验证码错误"),
    INVALID_USERNAME_PASSWORD(205, "账号或密码错误"),
    INVALID_RE_PASSWORD(206, "两次输入密码不一致"),
    INVALID_OLD_PASSWORD(207, "旧密码错误"),
    USERNAME_ALREADY_IN(208, "用户名已存在"),
    INVALID_USERNAME(209, "用户名不存在"),
    INVALID_ROLE(210, "角色不存在"),
    ROLE_USED(211, "角色使用中，不可删除"),
    NO_PERMISSION(403, "当前用户无该接口权限"),
    UNKNOWN_ERROR(2000, "未知错误"),
    PARAMETER_ILLEGAL(2001, "参数不合法"),
    TOKEN_INVALID(2002, "无效的Token"),
    TOKEN_SIGNATURE_INVALID(2003, "无效的签名"),
    TOKEN_EXPIRED(2004, "token已过期"),
    TOKEN_MISSION(2005, "token缺失"),
    REFRESH_TOKEN_INVALID(2006, "刷新Token无效");

    private Integer code;

    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
