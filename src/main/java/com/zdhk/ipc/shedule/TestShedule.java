package com.zdhk.ipc.shedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2021-05-18 17:59
 */
@Slf4j
//@Component
public class TestShedule {



    /**
     * 通过设置lockAtMostFor，我们可以确保即使节点死亡，锁也会被释放;通过设置lockAtLeastFor，
     * 我们可以确保它在9分钟内不会执行超过一次。请注意，对于执行任务的节点死亡的情况，
     * lockAtMostFor只是一个安全网，所以将它设置为一个时间，这个时间远远大于最大估计执行时间。
     * 如果任务花费的时间比lockAtMostFor更长，那么它可能会再次执行，结果将是不可预测的(更多的进程将持有锁)。
     */
    // @Scheduled(cron = "0 0 17 * * ?") 每天17点执行
    //@Scheduled(cron = "0/1 * * * * ?")
    @SchedulerLock(name = "scheduledTaskName", lockAtMostFor = "9m", lockAtLeastFor = "9m")
    public void SynchronousSchedule() {
        log.info("Start run schedule to synchronous data++++++++++++++");

    }


}
