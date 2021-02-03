package com.zdhk.ipc.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * DES，全称为“Data Encryption Standard”，中文名为“数据加密标准”
 *  明文按 64 位进行分组，密钥长 64 位，密钥事实上是 56 位参与 DES 运算
 *  （第8、16、24、32、40、48、56、64 位是校验位， 使得每个密钥都有奇数个 1）分组后的明文组和
 *  56 位的密钥按位替代或交换的方法形成密文组的加密方法.
 *
 *  和
 *  3DES三重DES
 */
public class MyDESUtils {

        private Cipher cipher;
        private SecretKey generateKey;

        public String encode(String src)
        {
            try
            {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
                keyGenerator.init(56);//size
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] keyBytes = secretKey.getEncoded();

                DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
                generateKey = secretKeyFactory.generateSecret(desKeySpec);

                cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
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


    public String des3Encode(String src)
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            keyGenerator.init(168);//size
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            DESedeKeySpec desKeySpec = new DESedeKeySpec(keyBytes);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            generateKey = secretKeyFactory.generateSecret(desKeySpec);

            cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
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

    public String des3decode(String src)
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
