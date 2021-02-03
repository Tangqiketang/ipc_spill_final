package com.zdhk.ipc.constant;

public class ConfigConst {

    public class auth{
        public static final String USER_ID ="userId";

        public static final String USER_NAME ="userName";

        public static final String TOKEN_EXPIRE_TIME ="tokenExpireTime";

        public static final String TOKEN_KEY ="token";
    }

    public  static final String SUPER_ADMIN ="#SUPERADMIN";

    public  static final Integer NORMAL_USER_ROLE =2;

    public  static final Integer NORMAL_MENU =6;


    public class cache{
        public static final String TERMINAL_TYPE_CACHE ="terminal:type";
    }

    public class sms{
        public static final String SMS_RECORD ="sms:record:";

        public static final String SMS_REQUEST_LIMIT ="sms:limit:";

        public static final String SMS_LIST_LIMIT ="sms:list:limit:";
    }
}
