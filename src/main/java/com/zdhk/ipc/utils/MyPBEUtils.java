package com.zdhk.ipc.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.SecureRandom;

/**
 * 对称
 * PBE，全称为“Password Base Encryption”，中文名“基于口令加密”
 * 把口令当做密钥,但是单纯的口令很容易被字典法给穷举出来，所以我们这里给口令加了点“盐”
 * 将盐和口令合并后用消息摘要算法(MD5或SHA等)进行迭代很多次来构建密钥初始化向量的基本材料
 */
public class MyPBEUtils {
    private Cipher cipher;
    private SecretKey generateKey;
    private PBEParameterSpec pbeParameterSpec;

    public String encode(String src)
    {
        try
        {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = secureRandom.generateSeed(8);

            String password = "amuro";
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWITHMD5andDES");
            generateKey = secretKeyFactory.generateSecret(pbeKeySpec);

            pbeParameterSpec = new PBEParameterSpec(salt, 100);
            cipher = Cipher.getInstance("PBEWITHMD5andDES");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey, pbeParameterSpec);
            byte[] resultBytes = cipher.doFinal(src.getBytes());
            return Hex.encodeHexString(resultBytes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String decode(String src)
    {
        try
        {
            cipher.init(Cipher.DECRYPT_MODE, generateKey, pbeParameterSpec);
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
