package com.zdhk.ipc.service;


import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.vo.WeChatLoginVO;

import java.math.BigDecimal;

public interface WXUserService {


    BaseResp<WeChatLoginVO> weChatLogin(String code, String encryptedData, String iv, BigDecimal latitude, BigDecimal longitude);

    String terminalActiveAndGetQRcode(BigDecimal longitude, BigDecimal latitude, String imei, Integer type);
}
