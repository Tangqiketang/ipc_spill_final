package com.zdhk.ipc.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 描述:高德地图
 *
 * @auther WangMin
 * @create 2021-05-17 13:37
 */
@Component
public class GeoMapUtils {

    private static final String gaodeKey = "b5a3c5a5ecc3c8ac3090d9b4c44dd5cc";

    @Resource
    private RestTemplate restTemplate;

    public  void regeo(BigDecimal startlongi,BigDecimal startlati){
        String url = "https://restapi.amap.com/v3/geocode/regeo";


        String startUrl = String.format("%s?key=%s&location=%s",url,gaodeKey,startlongi+","+startlati);
        String startResult = restTemplate.getForEntity(startUrl,String.class).getBody();
        String startPosition = JSON.parseObject(startResult).getJSONObject("regeocode").getString("formatted_address");


    }


}
