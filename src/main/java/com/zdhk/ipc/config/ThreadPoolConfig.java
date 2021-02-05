package com.zdhk.ipc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述:
 * 异步线程池
 *
 * @auther WangMin
 * @create 2021-02-04 17:00
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    //用来生成缩略图的线程池
    @Bean(name = "imageThreadPool")
    public ThreadPoolTaskExecutor imageThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        // 任务队列
        executor.setQueueCapacity(50);
        // 设置线程生存时间（秒）,当超过了核心线程出之外的线程在生存时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 设置线程名称前缀
        executor.setThreadNamePrefix("imagePool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //初始化
        executor.initialize();
        return executor;
    }

    //用来发邮件的线程池
    @Bean(name = "emailThreadPool")
    public ThreadPoolTaskExecutor emailThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数,它是可以同时被执行的线程数量
        executor.setCorePoolSize(2);
        // 设置最大线程数,缓冲队列满了之后会申请超过核心线程数的线程
        executor.setMaxPoolSize(10);
        // 设置缓冲队列容量,在执行任务之前用于保存任务
        executor.setQueueCapacity(50);
        // 设置线程生存时间（秒）,当超过了核心线程出之外的线程在生存时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 设置线程名称前缀
        executor.setThreadNamePrefix("emailPool-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //初始化
        executor.initialize();
        return executor;
    }

 /**********************************以下为具体使用方法*************************************************/

    //task.使用图片线程池
    @Async(value="imageThreadPool")
    public Future<String> asyncImage(int param) {
        long start= System.currentTimeMillis();
        try {
            //模拟执行耗时
            Thread.sleep(1000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        String result = param+"result";
        long end= System.currentTimeMillis();
        return new AsyncResult<>(result);
    }

    public List<String> testThreadPool() throws ExecutionException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int param = i;
            Future<String> future = asyncImage(param);  //执行多线程任务
            futures.add(future);
        }
        //获取所有任务的执行结果
        List<String> data = new ArrayList<>();
        for (Future future : futures) {
            String string = (String) future.get();
            data.add(string);
        }
        return data;
    }
}
