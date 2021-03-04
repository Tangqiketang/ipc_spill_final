package com.zdhk.ipc.controller;

import com.zdhk.ipc.constant.BASE_JSON_CODE;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.exception.ReqException;
import com.zdhk.ipc.service.LoginService;
import com.zdhk.ipc.utils.ValidateUtil;
import com.zdhk.ipc.vo.TerminalAccessVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *用户管理
 * @author Wang Min
 * @since 2020-11-12
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags={"用户管理"})
public class UserController {

    @Resource
    private LoginService loginService;

    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userName",value = "用户名", required = true),
            @ApiImplicitParam(name ="passWord",value = "密码", required = true)
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp login(@RequestParam(name = "userName", required = true)String userName,
                          @RequestParam(name = "passWord", required = true)String passWord){


        BaseResp rsp =  loginService.userLogin(userName,passWord);
        return rsp;
    }

    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userName",value = "用户名", required = true),
            @ApiImplicitParam(name ="passWord",value = "密码", required = true),
            @ApiImplicitParam(name ="confirmPassWord",value = "确认密码", required = true),
            @ApiImplicitParam(name ="smsCode",value = "短信验证", required = true),
    })
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp regist(@RequestParam(name = "userName", required = true)String userName,
                           @RequestParam(name = "passWord", required = true)String passWord,
                           @RequestParam(name = "confirmPassWord", required = true)String confirmPassWord,
                           @RequestParam(name = "smsCode", required = true)String smsCode){
        BaseResp rsp = new BaseResp();
        if(passWord.equals(confirmPassWord) == false){
            throw new ReqException(BASE_JSON_CODE.COMMON_FAIL.getDesc(),BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        if(ValidateUtil.validatePhone(userName) == false){
            throw new ReqException("输入的手机号格式不对",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        if(ValidateUtil.validatePassword(passWord) == false){
            throw new ReqException("输入的密码长度6-15位，数字、英文字母组合",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        String error = loginService.chekSmsCode(smsCode,userName);

        rsp =  loginService.userRegist(userName,passWord);
        return rsp;
    }

    @ApiOperation("用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp logout(){
        BaseResp rsp = new BaseResp();
        rsp.setDesc("登出成功");
        return rsp;
    }

    @ApiOperation("获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userName",value = "用户名", required = true),
            @ApiImplicitParam(name ="passWord",value = "密码", required = true)
    })
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public  BaseResp<TerminalAccessVO> getToken(@RequestParam(name = "userName", required = true)String userName,
                                                @RequestParam(name = "passWord", required = true)String passWord){
        BaseResp<TerminalAccessVO> rsp = new BaseResp<>();
        rsp =  loginService.getToken(userName,passWord);
        return rsp;
    }



    @ApiOperation("获取短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userName",value = "用户名", required = true)
    })
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp getSmsCode(@RequestParam(name = "userName", required = true)String userName){
        BaseResp rsp = new BaseResp<>();
        if(ValidateUtil.validatePhone(userName) == false){
            throw new ReqException("输入的手机号格式不对",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        //注册或忘记密码的短信,需要先校验用户是否存在
        loginService.userExsistCheck(userName,0);

        String smsCode = loginService.getSmsCode(userName);
        rsp.setDesc(smsCode);
        return rsp;
    }


    @ApiOperation("忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="userName",value = "用户名", required = true),
            @ApiImplicitParam(name ="passWord",value = "密码", required = true),
            @ApiImplicitParam(name ="confirmPassWord",value = "确认密码", required = true),
            @ApiImplicitParam(name ="confirmPassWord",value = "短信验证", required = true),
    })
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp resetPassword(@RequestParam(name = "userName", required = true)String userName,
                                  @RequestParam(name = "passWord", required = true)String passWord,
                                  @RequestParam(name = "confirmPassWord", required = true)String confirmPassWord,
                                  @RequestParam(name = "smsCode", required = true)String smsCode){
        BaseResp rsp = new BaseResp<>();
        if(passWord.equals(confirmPassWord) == false){
            throw new ReqException("两次输入的密码不一致",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        if(ValidateUtil.validatePhone(userName) == false){
            throw new ReqException("输入的手机号格式不对",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        if(ValidateUtil.validatePassword(passWord) == false){
            throw new ReqException("输入的密码长度6-15位，数字、英文字母组合",BASE_JSON_CODE.COMMON_FAIL.getCode());
        }
        //校验验证码
        String error = loginService.chekSmsCode(smsCode,userName);
        //修改密码
        rsp =  loginService.resetPassword(userName,passWord);
        return rsp;
    }


}

