package com.zdhk.ipc.constant;


import com.zdhk.ipc.constant.meta.ResponseTemplateEnum;

public enum ResponseEnum implements ResponseTemplateEnum {

    USER_NOT_EXIST(400, "用户不存在,请注册"),
    USER_PASSWORD_ERROR(400, "用户密码错误"),
    TOKEN_GET_SUCCESS(200, "token获取成功"),
    USER_NAME_EXIST(400, "用户已存在，请登录"),
    USER_LOCKED(400, "该用户已被锁定"),
    USER_PASSWORD_MUST(400, "用户密码必须填写"),
    USER_ID_NUMBER_ERROR(400, "用户身份证号不合法"),
    USER_NAME_SMS_LIMIT(400, "获取短信验证码频繁，请一分钟后再试"),
    USER_NAME_SMS_LIST_LIMIT(400, "次数已达上限"),
    USER_NAME_SMS_ERROR(400, "获取短信验证码失败"),
    CHECK_SMS_ERROR(400, "验证码无效"),
    CHECK_SMS_EXPIRE(400, "验证码失效"),
    VERSION_NOT_EXIST(400,"最新版本号不存在"),
    USER_NAME_SMS_OK(200, "获取短信验证码成功");
    public int code;
    public String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
