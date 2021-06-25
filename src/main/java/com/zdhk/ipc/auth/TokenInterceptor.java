package com.zdhk.ipc.auth;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zdhk.ipc.auth.context.UserContext;
import com.zdhk.ipc.auth.context.UserInfo;
import com.zdhk.ipc.constant.ConfigConst;
import com.zdhk.ipc.data.rsp.BaseResp;
import com.zdhk.ipc.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;

    public static String HEAD_TOKEN = "token";

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String url = request.getRequestURI();

        if (!(handler instanceof HandlerMethod)) { //拦截controller
            return true;
        }

        String token = request.getHeader(HEAD_TOKEN);
        if(RequestMethod.OPTIONS.name().equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }else{
            if(StringUtils.isBlank(token)){
                log.error("missing token");
                responseUnAuthReturn(response);
                return false;
            }
            try {
                DecodedJWT jwt = jwtUtil.verifyToken(token);
                Map<String, Claim> claimMap = jwt.getClaims();
                Date now = new Date();
                if(now.after(jwt.getExpiresAt())){
                    responseTimeOutReturn(response);
                    return false;
                }

                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(claimMap.get(ConfigConst.auth.USER_ID).asString());
                userInfo.setUserName(claimMap.get(ConfigConst.auth.USER_NAME).asString());
                UserContext.getUserInfo().set(userInfo);
                return true;
            }catch (Exception e){
                log.error("invalid token , please retry###",e);
                responseUnAuthReturn(response);
                return false;
            }
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    private void responseUnAuthReturn(HttpServletResponse response) throws IOException {
        BaseResp rsp = new BaseResp();
        rsp.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        rsp.setDesc("UnAuthorized");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(rsp));
    }

    private void responseTimeOutReturn(HttpServletResponse response) throws IOException {
        BaseResp rsp = new BaseResp();
        rsp.setCode(HttpServletResponse.SC_FORBIDDEN);
        rsp.setDesc("UnAuthorized");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSON.toJSONString(rsp));
    }

}
