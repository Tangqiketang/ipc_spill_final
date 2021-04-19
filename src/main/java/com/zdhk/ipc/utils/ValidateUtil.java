package com.zdhk.ipc.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
    private static final String REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";

    public static boolean validatePhone(String phone) {

        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18([0-1]|[5-9])))\\d{8}$";
        if(phone.length() != 11){
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(isMatch){
                return true;
            } else {
                return false;
            }
        }
    }


    public static boolean validatePassword(String password){
        return password.matches(REGEX);
    }

    public static boolean isNumeric(String str){
        if (StringUtils.isNotBlank(str)){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

}
