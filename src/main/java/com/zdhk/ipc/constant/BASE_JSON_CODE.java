package com.zdhk.ipc.constant;

public enum BASE_JSON_CODE {
    SUCCESS(0, "成功"),
    COMMON_FAIL(1, "普通失败"),
    TOKEN_EMPTY(2, "token为空"),
    TOKEN_VALIDATED(3, "token校验失败"),
    TOKEN_EXPIRE(4, "token过期"),
    APP_EXCEPTION(5, "程序异常");

    private int code;
    private String desc;

    private BASE_JSON_CODE(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
