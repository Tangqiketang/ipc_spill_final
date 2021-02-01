package com.zdhk.ipc.utils;

import java.security.SecureRandom;
import java.util.Random;

public class TokenUtil {


    public boolean checkToken(String sCallId) {
        Boolean bOverTime = false;
        String[] ut = sCallId.split("#");
        if (ut.length > 1 && MyDateUtils.getNow() > Integer.parseInt(ut[1], 16)) {
            bOverTime = true;
        }
        return bOverTime;
    }


    public String createNewToken(){
        int iDueTime = MyDateUtils.getNow() + 2 * 60 * 60;
        String token = generateSessionId();
        token = token +"#" +Integer.toHexString(iDueTime);
        return token;
    }


    public static String generateSessionId() {
        byte[] bytes = new byte[8];
        Random random = new SecureRandom();
        random.nextBytes(bytes);
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < bytes.length; ++i) {
            byte b1 = (byte) ((bytes[i] & 240) >> 4);
            byte b2 = (byte) (bytes[i] & 15);
            if (b1 < 10) {
                result.append((char) (48 + b1));
            } else {
                result.append((char) (48 + (b1 - 10)));
            }

            if (b2 < 10) {
                result.append((char) (48 + b2));
            } else {
                result.append((char) (48 + (b2 - 10)));
            }
        }

        return result.toString();
    }

}
