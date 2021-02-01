package com.zdhk.ipc.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    public static boolean isPhoneNum(String sPhoneNum) {
        boolean rtn = false;
        if (StringUtils.hasText(sPhoneNum)) {
            Pattern p = Pattern.compile("^((1[0-9]{2}))\\d{8}$");
            Matcher m = p.matcher(sPhoneNum);
            rtn = m.matches();
        }

        return rtn;
    }

    public static boolean isEmail(String email) {
        boolean rtn = false;
        if (StringUtils.hasText(email)) {
            Pattern pattern = Pattern.compile(
                    "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
            Matcher matcher = pattern.matcher(email);
            rtn = matcher.matches();
        }

        return rtn;
    }

    public static int getNow() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static Boolean chkTokenOverTime(String sUserToken) {
        Boolean bOverTime = false;
        String[] ut = sUserToken.split("#");
        if (ut.length > 1 && getNow() > Integer.parseInt(ut[1], 16)) {
            bOverTime = true;
        }
        return bOverTime;
    }

}
