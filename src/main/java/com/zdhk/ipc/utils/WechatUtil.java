package com.zdhk.ipc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.zdhk.ipc.constant.ConfigConst;
import com.zdhk.ipc.exception.ReqException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class WechatUtil {
    private final static Logger log = LoggerFactory.getLogger(WechatUtil.class);
    @Value("${wechat.checkToken}")
    private String checkToken;


    @Value("${wechat.mini.appID}")
    private String miniAppId;

    @Value("${wechat.mini.appsecret}")
    private String miniAppsecret;

    @Autowired
    private ObjectMapper objectMapper;

    @Resource
    RestTemplate restTemplate;

    @Resource
    RedisUtil redisUtil;



    public  JSONObject getSessionKeyOrOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>();
        // https://mp.weixin.qq.com/wxopen/devprofile?action=get_profile&token=164113089&lang=zh_CN
        //小程序appId
        requestUrlParam.put("appid", miniAppId);
        //小程序secret
        requestUrlParam.put("secret", miniAppsecret);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));
        return jsonObject;
    }

    public  JSONObject getMiniUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (Exception e) {
        }
        return null;
    }

    //@Scheduled(initialDelay = 10000, fixedRate = 6600000)
    public void creatAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String path = "?grant_type={grant_type}&appid={appid}&secret={secret}";
        Map<String, String> params = new HashMap<>(3);
        params.put("grant_type", "client_credential");
        params.put("appid", miniAppId);
        params.put("secret", miniAppsecret);
        ResponseEntity<String> forObject = restTemplate.getForEntity(url + path, String.class, params);
        JSONObject jsonObject = JSONObject.parseObject(forObject.getBody());
        String accessToken = jsonObject.getString("access_token");
        // accessToken获取成功
        if (accessToken != null) {
            //有效期设置1小时55分钟
            redisUtil.set(ConfigConst.mini.ACCESS_TOKEN_KEY, accessToken, 115 * 60);
        } else {
            redisUtil.set(ConfigConst.mini.ERROR_KEY, forObject.getBody());
        }
    }

    /**
     * 获取微信小程序accesstoken
     * @return
     */
    public String getAccessTokenFromRedis(){
        if(!redisUtil.hasKey(ConfigConst.mini.ACCESS_TOKEN_KEY)){
            creatAccessToken();
        }
        return redisUtil.get(ConfigConst.mini.ACCESS_TOKEN_KEY).toString();
    }







    /*******************************************公众号*********************************************************/

    /**
     * 微信公众号
     * @param code
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     */
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

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     * */
    public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                JSONObject userInfo = JSON.parseObject(result);
                return userInfo;
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
