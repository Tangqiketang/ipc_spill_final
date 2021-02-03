package com.zdhk.ipc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdhk.ipc.constant.ConfigConst;
import com.zdhk.ipc.constant.ResponseEnum;
import com.zdhk.ipc.data.constant.CommonConstant;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.dto.UserMenuDto;
import com.zdhk.ipc.entity.*;
import com.zdhk.ipc.exception.ReqException;
import com.zdhk.ipc.mapper.SysUserMapper;
import com.zdhk.ipc.service.LoginService;
import com.zdhk.ipc.service.UserService;
import com.zdhk.ipc.utils.*;
import com.zdhk.ipc.vo.AccessVO;
import com.zdhk.ipc.vo.TerminalAccessVO;
import com.zdhk.ipc.vo.UserMenuShowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-12-02 16:51
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    SmsUtils smsUtils;

    @Override
    public BaseResp userLogin(String userName, String passWord) {
        BaseResp<AccessVO> rsp = new BaseResp<>();
        SysUser sysUserExist = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,userName));
        if(sysUserExist == null){
            throw new ReqException(ResponseEnum.USER_NOT_EXIST.getMessage(),ResponseEnum.USER_NOT_EXIST.getCode());
        }
        if(!PasswordUtil.comparePassWord(sysUserExist.getPassword(),sysUserExist.getSalt(),passWord)){
            throw new ReqException(ResponseEnum.USER_PASSWORD_ERROR.getMessage(),ResponseEnum.USER_PASSWORD_ERROR.getCode());
        }
        if(sysUserExist.getStatus() == false){
            throw new ReqException(ResponseEnum.USER_LOCKED.getMessage(),ResponseEnum.USER_LOCKED.getCode());
        }
        AccessVO access = new AccessVO();

        Map<String,Object> tokenInfo = jwtUtil.createToken(sysUserExist.getId()+"",sysUserExist.getUsername());
        access.setToken((String)tokenInfo.get(ConfigConst.auth.TOKEN_KEY));
        access.setTokenExpire((Long)tokenInfo.get(ConfigConst.auth.TOKEN_EXPIRE_TIME));
        List<SysPermission> permissionList = new ArrayList<>();
        if(userService.isSuperAdmin(sysUserExist.getId())){
            permissionList = userService.getAllMenuList();
        }else{
            permissionList = userService.getUserMenuListByUserId(sysUserExist.getId());
        }
        List<UserMenuDto> userMenuList = FindsMenuChildrenUtil.wrapTreeDataToTreeList(permissionList);
        List<UserMenuShowVO> UserMenuShowList = new ArrayList<>();
        if(userMenuList != null && userMenuList.size() > 0){
            for (UserMenuDto userMenu : userMenuList) {
                UserMenuShowVO menuShow = CustomBeanUtil.map(userMenu, UserMenuShowVO.class);
                UserMenuShowList.add(menuShow);
            }
        }
        access.setMenuList(UserMenuShowList);
        access.setUserName(sysUserExist.getUsername());
        access.setAvatar(sysUserExist.getAvatar());
        access.setPhone(sysUserExist.getPhone());
        access.setRealName(access.getRealName());
        rsp.setResult(access);
        return rsp;
    }

    @Override
    public String chekSmsCode(String smsCode, String userName) {
        if(redisUtil.hasKey(ConfigConst.sms.SMS_RECORD+userName)){
            String code = (String) redisUtil.get(ConfigConst.sms.SMS_RECORD+userName);
            if(code != null && code.equals(smsCode)){
                return null;
            }else{
                throw new ReqException(ResponseEnum.CHECK_SMS_ERROR.getMessage(),ResponseEnum.CHECK_SMS_ERROR.getCode());
            }
        }else{
            throw new ReqException(ResponseEnum.CHECK_SMS_EXPIRE.getMessage(),ResponseEnum.CHECK_SMS_EXPIRE.getCode());
        }
    }

    @Override
    @Transactional
    public BaseResp<String> userRegist(String userName, String passWord) {
        BaseResp<String> rsp = new BaseResp<>();
        SysUser sysUserExist = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,userName));
        if(sysUserExist != null && sysUserExist.getUsername() != null){
            throw new ReqException("该手机号码已经被注册",400);
        }
        String salt = PasswordUtil.getSalt(10);
        String passWordMd5 = PasswordUtil.getPassWordMd5(passWord,salt);
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userName);
        sysUser.setPassword(passWordMd5);
        sysUser.setSalt(salt);
        sysUser.setPhone(userName);
        sysUser.setOrgCode("");
        sysUser.setStatus(true);
        sysUser.setDelFlag(false);
        sysUser.setCreateTime(new Date());
        sysUser.setSecid("0000000000");
        try {
            sysUser.insert();
            sysUser.setSecid(ConvertUtils.StringFill(sysUser.getId()+""));
            sysUser.updateById();
        }catch (Exception e){
            log.error("regist user fail",e);
            throw new ReqException("注册失败",400);
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(sysUser.getId());
        sysUserRole.setRoleId(ConfigConst.NORMAL_USER_ROLE);
        try {
            sysUserRole.insert();
        }catch (Exception e){
            log.error("regist user_role fail",e);
            throw new ReqException("注册失败",400);
        }
        SysRolePermission sysRolePermission = new SysRolePermission();
        sysRolePermission.setRoleId(sysUserRole.getRoleId());
        sysRolePermission.setPermissionId(ConfigConst.NORMAL_MENU);
        try {
            sysRolePermission.insert();
        }catch (Exception e){
            log.error("regist role_permission fail",e);
            throw new ReqException("注册失败",400);
        }

        TerminalAccessVO access = new TerminalAccessVO();
        Map<String,Object> tokenInfo = jwtUtil.createTokenTerminal(sysUser.getId()+"",sysUser.getUsername());
        access.setToken((String)tokenInfo.get(ConfigConst.auth.TOKEN_KEY));
        access.setTokenExpire((Long)tokenInfo.get(ConfigConst.auth.TOKEN_EXPIRE_TIME));

        rsp.setCode(0);
        rsp.setDesc("注册成功");
        return rsp;
    }


    @Override
    public BaseResp<TerminalAccessVO> getToken(String userName, String passWord) {
        BaseResp<TerminalAccessVO> rsp = new  BaseResp<>();
        SysUser sysUserExist = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,userName));
        if(sysUserExist == null){
            throw new ReqException(ResponseEnum.USER_PASSWORD_ERROR.getMessage(),ResponseEnum.USER_PASSWORD_ERROR.getCode());
        }
        if(!PasswordUtil.comparePassWord(sysUserExist.getPassword(),sysUserExist.getSalt(),passWord)){
            throw new ReqException(ResponseEnum.USER_PASSWORD_ERROR.getMessage(),ResponseEnum.USER_PASSWORD_ERROR.getCode());
        }
        if(sysUserExist.getStatus() == false){
            throw new ReqException(ResponseEnum.USER_LOCKED.getMessage(),ResponseEnum.USER_LOCKED.getCode());
        }
        TerminalAccessVO access = new TerminalAccessVO();
        Map<String,Object> tokenInfo = jwtUtil.createTokenTerminal(sysUserExist.getId()+"",sysUserExist.getUsername());
        access.setToken((String)tokenInfo.get(ConfigConst.auth.TOKEN_KEY));
        access.setTokenExpire((Long)tokenInfo.get(ConfigConst.auth.TOKEN_EXPIRE_TIME));
        rsp.setCode(0);
        rsp.setResult(access);
        rsp.setDesc("获取token成功");
        return rsp;
    }

    @Override
    public void userExsistCheck(String userName, int state) {
        BaseResp result = new BaseResp();
        if(state == 0){
            SysUser sysUserExist = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,userName));
            if(sysUserExist != null){
                throw new ReqException(ResponseEnum.USER_NAME_EXIST.getMessage(),ResponseEnum.USER_NAME_EXIST.getCode());
            }
        }
    }


    @Override
    public String getSmsCode(String userName) {
        //一分钟之内不能获取
        if( redisUtil.hasKey(ConfigConst.sms.SMS_REQUEST_LIMIT+userName)){
            throw new ReqException(ResponseEnum.USER_NAME_SMS_LIMIT.getMessage(),ResponseEnum.USER_NAME_SMS_LIMIT.getCode());
        }
        String smsCode = null;
        //一天只能获取5次
        if(redisUtil.hasKey(ConfigConst.sms.SMS_LIST_LIMIT+userName)){
            long size = redisUtil.lGetListSize(ConfigConst.sms.SMS_LIST_LIMIT+userName);
            if(size >= 5){
                throw new ReqException(ResponseEnum.USER_NAME_SMS_LIST_LIMIT.getMessage(),ResponseEnum.USER_NAME_SMS_LIST_LIMIT.getCode());
            }else{
                smsCode = smsUtils.sendSms(userName);
                if(smsCode == null || smsCode.equals("fail")){
                    throw new ReqException(ResponseEnum.USER_NAME_SMS_ERROR.getMessage(),ResponseEnum.USER_NAME_SMS_ERROR.getCode());
                }
                redisUtil.lSet(ConfigConst.sms.SMS_LIST_LIMIT+userName, 1);
            }
        }else{
            smsCode = smsUtils.sendSms(userName);
            if(smsCode == null || smsCode.equals("fail")){
                throw new ReqException(ResponseEnum.USER_NAME_SMS_ERROR.getMessage(),ResponseEnum.USER_NAME_SMS_ERROR.getCode());
            }
            long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            long tomorrowSecond= LocalDate.now().plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.of("+8"));

            redisUtil.lSet(ConfigConst.sms.SMS_LIST_LIMIT+userName,1,tomorrowSecond-nowSecond);
        }

        redisUtil.set(ConfigConst.sms.SMS_RECORD+userName, smsCode,60*10);
        redisUtil.set(ConfigConst.sms.SMS_REQUEST_LIMIT+userName, smsCode,60+10);
        return smsCode;

    }

    @Override
    public BaseResp resetPassword(String userName, String passWord) {
        BaseResp rsp = new BaseResp();
        SysUser sysUserExist = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,userName));
        if(sysUserExist == null){
            throw new ReqException("该手机号码未注册",400);
        }
        String salt = PasswordUtil.getSalt(10);
        String passWordMd5 = PasswordUtil.getPassWordMd5(passWord,salt);

        sysUserExist.setPassword(passWordMd5);
        sysUserExist.setSalt(salt);
        sysUserExist.setUpdateTime(new Date());

        try {
            sysUserExist.updateById();
        }catch (Exception e){
            throw new ReqException("重置密码失败",400);
        }

        rsp.setDesc("重置密码成功");
        return rsp;
    }

}
