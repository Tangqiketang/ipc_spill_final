package com.zdhk.ipc.utils;

import org.springframework.util.StringUtils;
import java.util.Base64;

/**
 * Base64算法基于64个基本字符，加密后的string中只包含这64个字符
 *
 */
public class MyBase64Utils {

        public static String encode(String src)
        {
            byte[] encodeBytes = Base64.getEncoder().encode(src.getBytes());
            return new String(encodeBytes);
        }

        public static String decode(String src)
        {
            byte[] decodeBytes = Base64.getDecoder().decode(src.getBytes());
            return new String(decodeBytes);
        }

    /**
     * alibaba使用Base64进行解密
     * @param res 密文
     * @return
     */
    public static String Base64Decode(String res) {
        if(StringUtils.hasText(res)){
            res = res.replaceAll("\r|\n", "");
        }else{
            return "";
        }
        return new String(com.alibaba.fastjson.util.Base64.decodeFast(res));
    }



}
