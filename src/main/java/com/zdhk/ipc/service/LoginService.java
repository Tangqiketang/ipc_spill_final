package com.zdhk.ipc.service;


import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.vo.TerminalAccessVO;

public interface LoginService {

    BaseResp userLogin(String userName, String passWord);

    String chekSmsCode(String smsCode, String userName);

    BaseResp userRegist(String userName, String passWord);

    BaseResp<TerminalAccessVO> getToken(String userName, String passWord);

    void userExsistCheck(String userName, int i);

    String getSmsCode(String userName);

    BaseResp resetPassword(String userName, String passWord);
}
