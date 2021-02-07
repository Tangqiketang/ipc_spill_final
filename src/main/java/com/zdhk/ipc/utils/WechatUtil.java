package com.zdhk.ipc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zdhk.ipc.exception.ReqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
public class WechatUtil {
    private final static Logger log = LoggerFactory.getLogger(WechatUtil.class);
    @Value("${wechat.checkToken}")
    private String checkToken;
    @Autowired
    private ObjectMapper objectMapper;

    public  String getopenId(String code, String appid, String secret) throws Exception {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code="
                + code + "&grant_type=" + "authorization_code";
        String result = HttpUtil.doGet(url,"UTF-8");
        log.info("getOpenId:"+result);
        ObjectNode fromObject = (ObjectNode) objectMapper.readTree(result);

        if (fromObject.path("openid") == null) {
            throw new ReqException("",fromObject.path("errcode").intValue());
        } else if (fromObject.path("access_token") == null) {
            throw new ReqException("",fromObject.path("errcode").intValue());
        } else {
            String accessToken = fromObject.path("access_token").asText();
            String openId = fromObject.path("openid").asText();

            //TODO 放入内存或redis

            return openId;
        }
    }


    /**
     * 校验签名
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 布尔值
     */
    public  boolean checkSignature(String signature,String timestamp,String nonce){
        String token = checkToken;  //平台配置的token
        String checktext = null;
        if (null != signature) {
            //对token,timestamp,nonce 按字典排序
            String[] paramArr = new String[]{token,timestamp,nonce};
            Arrays.sort(paramArr);
            //将排序后的结果拼成一个字符串
            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                //对接后的字符串进行sha1加密
                byte[] digest = md.digest(content.toString().getBytes());
                checktext = byteToStr(digest);
            } catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
        return checktext !=null ? checktext.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转化我16进制字符串
     * @param byteArrays 字符数组
     * @return 字符串
     */
    private  String byteToStr(byte[] byteArrays){
        String str = "";
        for (int i = 0; i < byteArrays.length; i++) {
            str += byteToHexStr(byteArrays[i]);
        }
        return str;
    }

    /**
     *  将字节转化为十六进制字符串
     * @param myByte 字节
     * @return 字符串
     */
    private  String byteToHexStr(byte myByte) {
        char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tampArr = new char[2];
        tampArr[0] = Digit[(myByte >>> 4) & 0X0F];
        tampArr[1] = Digit[myByte & 0X0F];
        String str = new String(tampArr);
        return str;
    }

}
