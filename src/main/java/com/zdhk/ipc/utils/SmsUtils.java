package com.zdhk.ipc.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.github.qcloudsms.SmsSingleSender;
import com.zdhk.ipc.dto.SmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SmsUtils {

    @Value("${qcloud.sms.appid}")
    private int appId;
    @Value("${qcloud.sms.appkey}")
    private String appKey;
    @Value("${qcloud.sms.templateid}")
    private int templateId;
    @Value("${qcloud.sms.smssign}")
    private String smsSign;
    @Value("${alicloud.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${alicloud.sms.accessSecret}")
    private String accessSecret;
    @Value("${alicloud.sms.signName}")
    private String signName;
    @Value("${alicloud.sms.templateCode}")
    private String templateCode;

    @Resource
    RedisUtil redisUtil;

    public String sendMessage(String phoneNumber) {
        String resultString = null;
        String code = RandomUtil.keyUtils();
        String[] params = {code};
        try {
            SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
            ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
            resultString = code;
        } catch (Exception e) {
            log.error("send sms fail ",e);
            resultString = "fail";
            return resultString;
        }
        return resultString;
    }


    public String sendSms(String phoneNumber){
        String resultString = null;
        String code = RandomUtil.keyUtils();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        SmsDto sms = new SmsDto();
        sms.setCode(code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSON(sms).toString());
        log.info(code);
        try {
            CommonResponse response = client.getCommonResponse(request);
            resultString = code;
            log.info(code);
        } catch (Exception e) {
            log.error("send sms fail ",e);
            resultString = "fail";
            return resultString;
        }
        return resultString;
    }
}
