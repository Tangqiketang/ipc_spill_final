package com.zdhk.ipc.exception;

import com.zdhk.ipc.exception.meta.ExceptionEnum;

/**
 * @Description:
 * @Auther: wangys
 * @Date: 9:13 2020/7/8
 */

public enum SysExceptionEnum implements ExceptionEnum {

    UNKNOWN_ERROR(-1, "未知错误"),
    BAD_REQUEST_PARAM_MISS(400, "非法请求，参数缺失"),
    BAD_REQUEST_PARAM_ERROR(400, "非法请求，参数错误"),
    BAD_REQUEST_PARAM_REPEAT(400, "重复请求"),
    SYSTEM_ERROR(500, "系统错误"),
    FEIGN_ERROR(500, "系统错误，参数转换失败");
    public int code;
    public String message;

    SysExceptionEnum(int code, String message) {
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
