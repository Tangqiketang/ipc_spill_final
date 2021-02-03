package com.zdhk.ipc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;


@Slf4j
public class RandomUtil {

    public static String keyUtils() {
        String code =  RandomStringUtils.random(4,false,true);
        log.info("smsCode:"+code);
        return code;
    }

}
