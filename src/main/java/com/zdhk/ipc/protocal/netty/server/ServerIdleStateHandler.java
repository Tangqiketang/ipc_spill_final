package com.zdhk.ipc.protocal.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * 空闲检测
 *
 * @auther WangMin
 * @create 2020-07-21 8:45
 */
@Slf4j
public class ServerIdleStateHandler extends IdleStateHandler {

    /** * 设置空闲检测时间为 30s */
    private static final int READER_IDLE_TIME = 30;

    public ServerIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    public ServerIdleStateHandler(Integer reader_idle_time) {
        super(reader_idle_time, 0, 0, TimeUnit.SECONDS);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info("{} 秒内没有读取到数据,关闭连接", READER_IDLE_TIME);
        ctx.channel().close();
    }
}
