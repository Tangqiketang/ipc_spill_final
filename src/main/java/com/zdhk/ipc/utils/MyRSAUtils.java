package com.zdhk.ipc.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 将两个大素数相乘十分容易，但是想要对其乘积进行因式分解却极其困难，
 * 因此可以将乘积公开作为加密密钥
 */
public class MyRSAUtils {
    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;

    public String encode(String src)
    {
        try
        {
            //初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();

            //私钥加密 公钥解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec
                    = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] resultBytes = cipher.doFinal(src.getBytes());

                    //私钥解密 公钥加密
        //  X509EncodedKeySpec x509EncodedKeySpec =
        //   new X509EncodedKeySpec(rsaPublicKey.getEncoded());
        //  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //  PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        //  Cipher cipher = Cipher.getInstance("RSA");
        //  cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //  byte[] resultBytes = cipher.doFinal(src.getBytes());

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
            //私钥加密 公钥解密
            X509EncodedKeySpec x509EncodedKeySpec =
                    new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));

                        //私钥解密 公钥加密
            //  PKCS8EncodedKeySpec pkcs8EncodedKeySpec
            //  = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            //  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //  PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            //  Cipher cipher = Cipher.getInstance("RSA");
            //  cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //  byte[] resultBytes = cipher.doFinal(Hex.decodeHex(src.toCharArray()));

            return new String(resultBytes);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 非对称加密已经灰常安全了，但是还有一个破绽：
     * 服务器A公布了自己的公钥，我的电脑是用服务器A的公钥加密数据后再发给服务器A的；
     * 这时候服务器B侵入了我的电脑，把我用来加密的公钥换成了它的公钥，于是我发出去的数据就会被服务器B的私钥破解了。
     * 肿么防止公钥被篡改呢？
     * 对，我们想到了前面的消息摘要，服务器A把公钥丢给我的时候，同时去CA申请一份数字证书，其实主要就是公钥的消息摘要，
     * 有了这份证书，当我再用公钥加密的时候，我就可以先验证一下当前的公钥是否确定是服务器A发送给我的。
     * @param src
     * @return
     */
    public static boolean verifySign(String src)
    {
        try
        {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            PrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec
                    = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            //生成签名bytes
            byte[] signBytes = signature.sign();

            X509EncodedKeySpec x509EncodedKeySpec =
                    new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean isVerified = signature.verify(signBytes);

            return isVerified;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
