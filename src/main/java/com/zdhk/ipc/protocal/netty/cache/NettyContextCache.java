package com.zdhk.ipc.protocal.netty.cache;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:
 * 用于保存客户端会话及相应读写操作
 *
 * @auther WangMin
 * @create 2020-07-22 13:28
 */
@Slf4j
public class NettyContextCache {
    /**
     * 管理一个全局map会话，保存连接进服务端的通道数量。可以改为<key=设备序列号，value=会话>
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,ChannelId> ADDR_MAP = new ConcurrentHashMap<>();

    public static ChannelHandlerContext getChannelContextByDeviceAddr(String addr){
        if(ADDR_MAP.containsKey(addr)){
            if(null!=ADDR_MAP.get(addr)){
                return CHANNEL_MAP.get(ADDR_MAP.get(addr));
            }
        }
        return null;
    }

    public static boolean checkAddrConnectExist(String addr){
       return ADDR_MAP.containsKey(addr);
    }

    public static void putDeviceAddrAndChannelContext(String addr, ChannelHandlerContext context){
        CHANNEL_MAP.put(context.channel().id(),context);
        ADDR_MAP.put(addr,context.channel().id());
    }

    public static void removeChannelId(ChannelId channelId){
        CHANNEL_MAP.remove(channelId);
        /*同时删除设备唯一标识和通道的映射关系*/
        ADDR_MAP.values().removeIf(value->value==channelId);
    }

    public static boolean containsContext(ChannelId channelId){
        return CHANNEL_MAP.containsKey(channelId);
    }

    public static void putChannelIdAndContext(ChannelId channelId,ChannelHandlerContext context){
        CHANNEL_MAP.put(channelId,context);
    }

    public static ChannelHandlerContext getChannelContextByChannelId(ChannelId channelId){
        return CHANNEL_MAP.get(channelId);
    }

    public static int getConnectScoketCount(){
        return CHANNEL_MAP.size();
    }

    public static ChannelFuture writeAndFlushByDeviceAddr(String addr,String response){
        ChannelFuture future = null;
        try{
            ChannelHandlerContext context  = getChannelContextByDeviceAddr(addr);
            if(null!=context){
                future = context.writeAndFlush(response);
                //context.write(response);
                //context.flush();
            }else{
                log.error("addr:{}发送数据失败,数据为{}",addr,response);
            }
        }catch (Exception e){
            log.error("addr:{}发送数据失败,数据为{}",addr,response);
        }
        return future;
    }

}
