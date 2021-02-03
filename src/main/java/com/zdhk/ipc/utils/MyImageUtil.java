package com.zdhk.ipc.utils;

import sun.applet.Main;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-11-11 15:44
 */
public class MyImageUtil {


    public static String byte2StringEncodeBase64(byte[] bys){
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bys);
    }


    public static byte[] string2byteDecodeBase64(String s){
        byte[] decoderBytes = null;
        // 对字节数组Base64编码
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            decoderBytes = decoder.decodeBuffer(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decoderBytes;
    }



    public static long getNumberThree(List<String> intarray, Long number){
        long index = Math.abs(number-Long.valueOf(intarray.get(0)));
        long result = Long.valueOf(intarray.get(0));
        for (String i : intarray) {
            long abs = Math.abs(number-Long.valueOf(i));
            if(abs <= index){
                index = abs;
                result = Long.valueOf(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("10");
        list.add("9");
        list.add("100");
        list.add("55");
        System.out.println(getNumberThree(list,60l));
    }


}
