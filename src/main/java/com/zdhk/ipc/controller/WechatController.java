package com.zdhk.ipc.controller;


import com.alibaba.fastjson.JSONObject;
import com.zdhk.ipc.auth.context.UserContext;
import com.zdhk.ipc.data.constant.Actions;
import com.zdhk.ipc.data.constant.ResultCode;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.exception.ReqException;
import com.zdhk.ipc.service.WXUserService;
import com.zdhk.ipc.utils.WechatUtil;
import com.zdhk.ipc.vo.UserInfoVO;
import com.zdhk.ipc.vo.WeChatLoginVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;

@Controller
@Slf4j
public class WechatController {

    @Value("${wechat.appID}")
    private String appID;

    @Value("${wechat.appsecret}")
    private String appsecret;

    @Value("${wechat.redirect_Uri}")
    private String redirect_Uri;

    @Value("${wechat.mini.appID}")
    private String miniAppId;

    @Value("${wechat.mini.appsecret}")
    private String miniAppsecret;

    @Autowired
    private WechatUtil wechatUtil;

    @Resource
    private WXUserService wxUserService;

    @ApiOperation("微信小程序登录")
    @RequestMapping(value = "/mini/weChatLogin", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<WeChatLoginVO> weChatLogin(
            @ApiParam(value = "微信返回的code", required = true) @RequestParam(value = "code") String code,
            @ApiParam(value = "加密数据", required = true) @RequestParam(value = "encryptedData") String encryptedData,
            @ApiParam(value = "加密密钥", required = true) @RequestParam(value = "iv") String iv,
            @ApiParam(value = "纬度", required = true) @RequestParam(value = "latitude") BigDecimal latitude,
            @ApiParam(value = "经度", required = true) @RequestParam(value = "longitude") BigDecimal longitude) {
        BaseResp res=new BaseResp();

        BaseResp<WeChatLoginVO> result = wxUserService.weChatLogin(code, encryptedData, iv, latitude, longitude);

        return result;
    }

    @ApiOperation("微信获取用户信息")
    @RequestMapping(value = "/mini/getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public BaseResp<UserInfoVO> weChatQueryUserInfo() {
        BaseResp bj = new BaseResp();
        Long userId = Long.valueOf(UserContext.getUserInfo().get().getUserId());
        UserInfoVO userInfoVO;

        return bj;
    }


    /********************************************公众号*******************************************************/

    /**
     * 微信公众号
     * @param action
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/aems/api/1/{action}", method= RequestMethod.POST, produces="application/json; charset=UTF-8")
    public @ResponseBody
    BaseResp userBase(@PathVariable String action, @RequestBody String req) throws Exception {
        log.info("userBase.in.req:[{}],|action:[{}]",req,action);
        BaseResp rspBean = new BaseResp();

        switch(action){
            case Actions.USER_INFO:
                rspBean = this.getUserInfo(req);
                break;
            default:
                throw new ReqException("action.is.wrong", ResultCode.ACCESS_DENIED);
        }
        log.info("userBase.out.response:[{}],|action:[{}]",rspBean.toString(),action);
        return rspBean;
    }

    private BaseResp getUserInfo(String reqStr) {
        JSONObject req = JSONObject.parseObject(reqStr);

        BaseResp resp = new BaseResp();

        String code = req.getJSONObject("condition").getString("sOpenCode");
        String sOpenId = null;
        try {
            sOpenId = wechatUtil.getopenId(code, appID, appsecret);
        }catch (Exception e){
            throw new ReqException("",900);
        }

        JSONObject ojd = new JSONObject();
        ojd.put("sOpenId",sOpenId);

        resp.setResult(ojd);
        return resp;
    }


    /**
     * 步骤一、公众平台和本服务绑定验证
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/aems/checkToken")
    public @ResponseBody
    void checkToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //token验证代码段
        try{
            log.info("请求已到达，开始校验token");
            if (null!=request.getParameter("signature")) {
                String signature = request.getParameter("signature"); //校验的最终值
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String echostr = request.getParameter("echostr");
                log.info("signature[{}], timestamp[{}], nonce[{}], echostr[{}]", signature, timestamp, nonce, echostr);
                if (wechatUtil.checkSignature(signature, timestamp, nonce)) {
                    log.info("数据源为微信后台，将echostr[{}]返回！", echostr);
                    response.getOutputStream().println(echostr);
                }
            }
        }catch (IOException e){
            log.error("校验出错");
            e.printStackTrace();
        }


    }

    /**
     * 步骤二、用户网页授权。前端向后端请求code,
     * 后台向微信平台获取code(微信会让用户确认，获取用户信息)并发送到指定的页面
     * @throws Exception
     */
    @RequestMapping(value="/aems/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        //这里要将你的授权回调地址处理一下，否则微信识别不了
        String redirectUri= URLEncoder.encode(redirect_Uri, "UTF-8");
        //简单获取openid的话参数response_type与scope与state参数固定写死即可
        StringBuffer url=new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize?redirect_uri="+redirectUri+
                "&appid="+appID+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect");
        response.sendRedirect(url.toString());
    }




    /**
     * 步骤三、接受微信平台发送code的页面。 接受微信发送过来的code，并带往本地页面
     * @param request
     * @param mv
     * @return
     */
    @RequestMapping(value="/aems/wxAuth")
    public ModelAndView wxAuth(HttpServletRequest request, ModelAndView mv){
        String code = request.getParameter("code");
        String state = request.getParameter("STATE");
        log.info("wxAuth.ininnin++++++++++++=code:"+code+"|||state:"+state);
        mv.setViewName("wxAuth");
        mv.addObject("code",code);
        return mv;

    }


}