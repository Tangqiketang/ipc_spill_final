package com.zdhk.ipc.protocal.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zdhk.ipc.entity.WebsocketInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每个用户连接之后保存会话到全局变量
 */
@ServerEndpoint("/imserver/{userId}")
@Component
@Slf4j
@Api(value="websokcet接口",tags={"websokcet接口"})
public class WebSocketServer {
    private static int onlineCount = 0; //记录当前连接数

    //用于存放每个用户对应的websokcet对象,全局唯一
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    //与每个客户端的连接会话.非静态，每个websocketServer对象中保存一个独立的session
    private Session session;

    private String userId = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId){
        this.session = session;
        this.userId = userId;
        //保存或刷新会话
        if(webSocketMap.containsKey(userId)){
            //刷新
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }else{
            webSocketMap.put(userId,this);
            synchronized (WebSocketServer.class){
                WebSocketServer.onlineCount++;
            }
        }
        log.info("连接用户："+userId);
        try{
            sendMessage("连接成功");
        }catch (Exception e){

        }
    }

    @OnClose
    public void onclose(){
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            synchronized (WebSocketServer.class){
                //总人数减一
                WebSocketServer.onlineCount--;
            }
        }
        log.info("用户推出:"+userId);
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息。消息可以保存到数据库或redis
        if(StringUtils.hasText(message)){
            try{
                JSONObject obj = JSON.parseObject(message);
                obj.put("fromUserId",this.userId);
                String toUserId = obj.getString("toUserId");
                //传送给对应的toUserId用户的websocket
                if(StringUtils.hasText(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(obj.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在线");
                    //可以保存到redis作为留言
                    webSocketMap.get(this.userId).sendMessage("当前用户不在线");
                }
            }catch (Exception e){
            }
        }

    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 实现主动推送
     * @param msg
     * @throws IOException
     */
    private void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    /**
     * 根据用户ID主动发起消息推送
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.hasText(userId)&&webSocketMap.containsKey(userId)){
            JSONObject obj = new JSONObject();
            obj.put("contentText",message);
            obj.put("toUserId",userId);
            obj.put("type",1);
            webSocketMap.get(userId).sendMessage(obj.toJSONString());
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    @ApiOperation(value = "websocket发送的消息体结构")
    public static void sendInfo2AllClient(WebsocketInfo message){
        String msg = JSON.toJSONString(message);
        log.info("websocket.send:"+msg);
        if(CollectionUtils.isNotEmpty(webSocketMap)){
            for(WebSocketServer server : webSocketMap.values()){
                try {
                    server.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
