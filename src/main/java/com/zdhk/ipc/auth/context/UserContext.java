package com.zdhk.ipc.auth.context;

public class UserContext {



    private static final ThreadLocal<UserInfo> userInfo = new  ThreadLocal<>();

    public static ThreadLocal<UserInfo> getUserInfo() {
        return userInfo;
    }
}
