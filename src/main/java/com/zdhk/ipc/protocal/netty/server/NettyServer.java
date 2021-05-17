package com.zdhk.ipc.protocal.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
/**
 * 描述:
 * netty服务端 ms
 *
 * @auther WangMin
 * @create 2020-07-21 8:45
 */
@Slf4j
@Service
public class NettyServer implements CommandLineRunner {


    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = this.start();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                destroyAll();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }



    /**
     *  两个独立的Reactor线程池。
     * 一个用于接收客户端的TCP连接，
     * 另一个用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    @Value("${netty.server.ip}")
    private String serverIp;

    @Value("${netty.server.port}")
    private Integer serverPort;


    public ChannelFuture start(){
        ChannelFuture f = null;
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                   // .localAddress(new InetSocketAddress(serverIp,serverPort))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ServerIdleStateHandler(30*3));  //3次心跳空闲检测
                            //设置特殊分隔符0x03
                            ByteBuf[] buf = new ByteBuf[]{Unpooled.wrappedBuffer(new byte[]{3})};
                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*6, buf));
                           // channel.pipeline().addLast(new LsEncoder());
                            channel.pipeline().addLast(nettyServerHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)  //服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝
                    .option(ChannelOption.SO_SNDBUF, 32*1024)	//发送缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32*1024)	//接收缓冲大小
                    .childOption(ChannelOption.SO_KEEPALIVE,true);//已加入空闲检测
            f = bootstrap.bind(serverPort).sync();
            channel = f.channel();
            log.info("======EchoServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("Netty server listening {} on port {} and ready for connections...",serverIp,serverPort);
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return f;
    }

    /**
     * 停止服务
     */
    public void destroyAll() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }


}
