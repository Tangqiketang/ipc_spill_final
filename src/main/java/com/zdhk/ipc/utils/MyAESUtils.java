package com.zdhk.ipc.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 全称为“Advanced Encryption Standard”，中文名“高级加密标准”，
 * 在密码学中又称 Rijndael 加密法
 * AES 设计有三个密钥长度：128，192，256 位。
 * 相对而言，AES 的 128 密钥比 DES 的 56 密钥强了 1021 倍。
 */
public class MyAESUtils {
    private Cipher cipher;
    private SecretKey generateKey;

    public String encode(String src)
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);//size
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            generateKey = new SecretKeySpec(keyBytes, "AES");

            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey);
            byte[] resultBytes = cipher.doFinal(src.getBytes());

            return Hex.encodeHexString(resultBytes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String decode(String src)
    {
        try
        {
            cipher.init(Cipher.DECRYPT_MODE, generateKey);
            byte[] result = Hex.decodeHex(src.toCharArray());
            return new String(cipher.doFinal(result));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
