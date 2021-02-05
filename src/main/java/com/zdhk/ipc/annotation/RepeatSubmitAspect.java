package com.zdhk.ipc.annotation;

import com.zdhk.ipc.auth.TokenInterceptor;
import com.zdhk.ipc.config.redis.RedisLock;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.exception.SysExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 描述:
 * AOP类解析注解-配合redis-解决程序集群部署时请求可能会落到多台机器上的问题
 *
 * @auther WangMin
 * @create 2021-02-05 11:25
 */
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    @Autowired
    private RedisLock redisLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    /**
     * token（或者JSessionId）+ 当前请求地址，作为一个唯一 KEY，
     * 去获取 Redis 分布式锁（如果此时并发获取，只有一个线程会成功获取锁。）
     */
    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Assert.notNull(request, "request can not null");

        // 此处可以用token或者JSessionId
        String token = request.getHeader(TokenInterceptor.HEAD_TOKEN);
        String path = request.getServletPath();
        String key = token + path;
        String uuid = UUID.randomUUID().toString();

        boolean isSuccess = redisLock.tryLock(key, uuid, noRepeatSubmit.lockTime()); //key-uudi
        log.info("tryLock key = [{}], uuid = [{}]", key, uuid);
        // 主要逻辑
        if (isSuccess) {
            log.info("tryLock success, key = [{}], uuid = [{}]", key, uuid);
            // 获取锁成功
            Object result;
            try {
                // 执行进程
                result = pjp.proceed();
            } finally {
                redisLock.releaseLock(key, uuid);
                log.info("releaseLock success, key = [{}], uuid = [{}]", key, uuid);
            }
            return result;
        } else {
            // 获取锁失败，认为是重复提交的请求。
            log.info("tryLock fail, key = [{}]", key);
            return new BaseResp<>(SysExceptionEnum.BAD_REQUEST_PARAM_REPEAT);

        }
    }



}
