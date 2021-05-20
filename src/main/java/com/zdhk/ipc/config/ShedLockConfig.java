package com.zdhk.ipc.config;


/**
 * 分布式定时调度配置
 * <p>
 * SchedulerLock 注解一共支持五个参数，分别是
 * name 用来标注一个定时服务的名字，被用于写入数据库作为区分不同服务的标识，如果有多个同名定时任务则同一时间点只有一个执行成功
 * lockAtMostFor 成功执行任务的节点所能拥有独占锁的最长时间，单位是毫秒ms
 * lockAtMostForString 成功执行任务的节点所能拥有的独占锁的最长时间的字符串表达，例如“PT14M”表示为14分钟
 * lockAtLeastFor 成功执行任务的节点所能拥有独占所的最短时间，单位是毫秒ms
 * lockAtLeastForString 成功执行任务的节点所能拥有的独占锁的最短时间的字符串表达，例如“PT14M”表示为14分钟
 */

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT30M")
public class ShedLockConfig {

    @Bean
    public LockProvider lockProvider(RedisTemplate redisTemplate) {
        return new RedisLockProvider(redisTemplate.getConnectionFactory());

    }

}
