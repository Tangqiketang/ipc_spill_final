package com.zdhk.ipc.config.redis;

import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2021-02-05 13:39
 */
@Service
public class RedisLock {

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    // 当前设置 过期时间单位, EX = seconds; PX = milliseconds
    private static final String SET_WITH_EXPIRE_TIME = "EX";

    /**
     * 释放锁脚本，原子操作，lua脚本
     */
    private static final String UNLOCK_LUA;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }


    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁
     * 对于 Redis 集群则无法使用
     * 支持重复，线程安全
     * @param lockKey   加锁键
     * @param uuid  加锁客户端唯一标识(采用UUID)
     * @param seconds   锁过期时间
     * @return
     */
    public boolean tryLock(String lockKey, String uuid, long seconds) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {

         return redisConnection.set(lockKey.getBytes(StandardCharsets.UTF_8),
                             uuid.getBytes(StandardCharsets.UTF_8),
                             Expiration.seconds(seconds),
                             RedisStringCommands.SetOption.SET_IF_ABSENT);
        });
    }

    /**
     * 与 tryLock 相对应，用作释放锁
     *
     * @param lockKey
     * @param uuid
     * @return
     */
    public boolean releaseLock(String lockKey, String uuid) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            return   redisConnection.eval(UNLOCK_LUA.getBytes(),
                                  ReturnType.BOOLEAN,
                                 1,
                                  lockKey.getBytes(StandardCharsets.UTF_8),
                                  uuid.getBytes(StandardCharsets.UTF_8));
        });
    }
}
