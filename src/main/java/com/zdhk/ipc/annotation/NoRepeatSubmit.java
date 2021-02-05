package com.zdhk.ipc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动幂等注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {
    /*
     * 防止重复提交标记注解
     * 设置请求锁定时间
     * @return
     */
    int lockTime() default 10;

}
