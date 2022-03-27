package com.zdhk.ipc.utils.sign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Base64Utils;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-01-25 17:23
 */
public class SignUtil {

    // 鸿雁颁发的 AppSecret
    private final static String AppSecret = "testSecret";

    /**
     * 签名方法
     * @param param 请求参数
     * @param randomKey 随token一起颁发的随机码  AppSecret
     * @param Timestamp 请求头中13位时间戳
     * @param SignatureNonce 请求头中的唯一随机数
     * @return 返回本次http请求的body
     */
    public static BaseTransferEntity sign(JSONObject param,
                                          String randomKey,
                                          String Timestamp,
                                          String SignatureNonce) {

        // 将参数构建成JSON字符串
        String jsonString = JSONObject.toJSONString(param);

        // Base64编码
        String object = Base64Utils.encodeToString(jsonString.getBytes());

        // 签名
        String sign = MD5Util.encrypt(object + randomKey + AppSecret + Timestamp + SignatureNonce);

        // 构建基本传输对象
        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setObject(object);
        baseTransferEntity.setSign(sign);

        return baseTransferEntity;
    }

    /**
     * 基础的传输bean
     */
    public static class BaseTransferEntity {
        private String object; //base64编码的json字符串
        private String sign;   //签名

        public String getObject() {
            return object;
        }
        public void setObject(String object) {
            this.object = object;
        }
        public String getSign() {
            return sign;
        }
        public void setSign(String sign) {
            this.sign = sign;
        }
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
        JSONObject param = new JSONObject();
        param.put("phone", "187xxxxxxxxx");
        BaseTransferEntity body = SignUtil.sign(param, "", "1", "1");
        System.out.println(JSONObject.toJSONString(body));
    }
}