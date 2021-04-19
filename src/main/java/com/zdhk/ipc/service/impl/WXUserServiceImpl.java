package com.zdhk.ipc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdhk.ipc.constant.ConfigConst;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.entity.SysRolePermission;
import com.zdhk.ipc.entity.SysUser;
import com.zdhk.ipc.entity.SysUserRole;
import com.zdhk.ipc.exception.ReqException;
import com.zdhk.ipc.mapper.*;
import com.zdhk.ipc.service.WXUserService;
import com.zdhk.ipc.utils.ConvertUtils;
import com.zdhk.ipc.utils.JwtUtil;
import com.zdhk.ipc.utils.MyFileUtils;
import com.zdhk.ipc.utils.WechatUtil;
import com.zdhk.ipc.vo.WeChatLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2020-12-02 17:10
 */
@Service
@Slf4j
public class WXUserServiceImpl implements WXUserService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysPermissionExtMapper sysPermissionExtMapper;
    @Resource
    private SysRolePermissionMapper rolePermissionMapper;
    @Resource
    private WechatUtil wechatUtil;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RestTemplate restTemplate;
    @Value("${app.location.win}")
    private String winLocation;

    @Value("${app.location.lin}")
    private String linLocation;


    @Override
    public BaseResp<WeChatLoginVO> weChatLogin(String code, String encryptedData, String iv, BigDecimal latitude, BigDecimal longitude) {
        BaseResp result = new BaseResp();
        JSONObject sessionKeyOpenId = wechatUtil.getSessionKeyOrOpenId(code);
        String openId = sessionKeyOpenId.getString("openid");
        String sessionKey = sessionKeyOpenId.getString("session_key");
        //解密微信返回用户信息
        JSONObject userInfo = wechatUtil.getUserInfo(encryptedData, sessionKey, iv);
        if (userInfo == null || StringUtils.isBlank(userInfo.getString("phoneNumber"))) {
            throw new ReqException("没有获取用户名与手机号+++code=" + code + "+++sessionKey=" + sessionKey + "+++openid=" + openId,400);
        }
        String username = userInfo.getString("phoneNumber");
        //TODO 根据openid和手机号查询用户是否新用户,如果是新用户SysUser新增到数据库,用户分配角色SysUserRole。添加到wechat-user表
        boolean existUser = true;
        if (!existUser) {
            //用户信息入库,！！！这里先查询用户使用已经存在sys_user表中,可能平台已经注册,微信没注册
            SysUser sysUser = new SysUser();
            sysUser.setSecid("0000000000");
            sysUser.setUsername(username);
            sysUser.setPhone(username);
            sysUser.setStatus(true);
            sysUser.setDelFlag(false);
            sysUser.setCreateTime(new Date());
            try {
                //新增用户
                sysUser.insert();
                sysUser.setSecid(ConvertUtils.StringFill(sysUser.getId() + ""));
                sysUser.updateById();
                //wechat-user表增加绑定关系 TODO
            } catch (Exception e) {
                throw new ReqException("添加用户失败",400);
            }
            //用户分配默认角色
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(ConfigConst.NORMAL_USER_ROLE);
            try {
                sysUserRole.insert();
            } catch (Exception e) {
               throw new ReqException("添加用户角色失败",400);
            }
            //角色分配默认权限
            List<SysRolePermission> perminssionList = rolePermissionMapper.selectList(
                    new LambdaQueryWrapper<SysRolePermission>()
                            .eq(SysRolePermission::getRoleId, sysUserRole.getRoleId())
                            .eq(SysRolePermission::getPermissionId, ConfigConst.NORMAL_MENU));
            if (CollectionUtils.isEmpty(perminssionList)) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setRoleId(sysUserRole.getRoleId());
                sysRolePermission.setPermissionId(ConfigConst.NORMAL_MENU);
                try {
                    sysRolePermission.insert();
                } catch (Exception e) {
                    throw new ReqException("添加角色权限失败",400);
                }
            }
        }
        //无论是注册还是登陆都更新登录时间
        SysUser sysUserExist = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        //无论是注册还是登录都更新token
        WeChatLoginVO weChatLoginVo = new WeChatLoginVO();
        Map<String, Object> tokenInfo = jwtUtil.createToken(sysUserExist.getId() + "", sysUserExist.getUsername());
        weChatLoginVo.setToken((String) tokenInfo.get(ConfigConst.auth.TOKEN_KEY));
        weChatLoginVo.setTokenExpire((Long) tokenInfo.get(ConfigConst.auth.TOKEN_EXPIRE_TIME));
        weChatLoginVo.setOpenId(openId);
        result.setResult(weChatLoginVo);
        return result;
    }

    @Override
    public String terminalActiveAndGetQRcode(BigDecimal longitude, BigDecimal latitude, String imei, Integer type) {
        String filePath =  this.getMiniQRCodePath(imei);

        return filePath;
    }

    private String getMiniQRCodePath(String imei){
        String os = System.getProperties().getProperty("os.name");
        String path = !os.startsWith("win") && !os.startsWith("Win") ? linLocation : winLocation;
        String filePath = path+imei+".jpeg";

        File file=new File(filePath);
        if(file.exists()){
            return filePath;
        }

        //将小程序和imei二维码保存到服务器
        String accessToken = wechatUtil.getAccessTokenFromRedis();
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("scene",imei);
        HttpEntity httpEntity = new HttpEntity(params,headers);
        ResponseEntity<String> request = restTemplate.postForEntity(url, httpEntity,String.class);
        String result = request.getBody();

        MyFileUtils.writeFile(filePath,new ByteArrayInputStream(result.getBytes(StandardCharsets.ISO_8859_1)));

        return filePath;
    }
}
