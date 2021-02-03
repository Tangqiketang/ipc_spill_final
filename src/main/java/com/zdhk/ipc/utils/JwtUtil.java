package com.zdhk.ipc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zdhk.ipc.constant.ConfigConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${token.expireTime}")
    private long EXPIRE_TIME;

    @Value("${token.expireTime}")
    private long TERMINAL_EXPIRE_TIME;
    @Value("${token.secret}")
    private String secret;

    public Map<String, Object> createToken(String userId,String userName){
        Map<String,Object> tokenInfoMap = new HashMap<>();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        long tokenExpire =System.currentTimeMillis() + EXPIRE_TIME;
        Date date = new Date(tokenExpire);
        String token = JWT.create().withClaim(ConfigConst.auth.USER_NAME, userName).withClaim(ConfigConst.auth.USER_ID, userId).withExpiresAt(date).sign(algorithm);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_KEY,token);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_EXPIRE_TIME,tokenExpire);
        return tokenInfoMap;

    };

    public Map<String, Object> createTokenTerminal(String userId,String userName){
        Map<String,Object> tokenInfoMap = new HashMap<>();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        long tokenExpire =System.currentTimeMillis() + TERMINAL_EXPIRE_TIME;
        Date date = new Date(tokenExpire);
        String token = JWT.create().withClaim(ConfigConst.auth.USER_NAME, userName).withClaim(ConfigConst.auth.USER_ID, userId).withExpiresAt(date).sign(algorithm);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_KEY,token);
        tokenInfoMap.put(ConfigConst.auth.TOKEN_EXPIRE_TIME,tokenExpire);
        return tokenInfoMap;

    };

    public DecodedJWT verifyToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt =verifier.verify(token);
        return jwt;
    }
}
