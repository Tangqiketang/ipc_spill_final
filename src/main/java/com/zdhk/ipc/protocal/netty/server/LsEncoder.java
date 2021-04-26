package com.zdhk.ipc.protocal.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 描述:
 * 自定义编码器
 *https://www.jianshu.com/p/23d7a8396bfa
 * @auther WangMin
 * @create 2020-07-28 10:25
 */
public class LsEncoder extends MessageToByteEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, ByteBuf out) throws Exception {
        out.writeByte(0x02);
        out.writeBytes(msg.getBytes());
        out.writeByte(0x03);
    }
}
