package com.hhu.filmix.util;

import cn.dev33.satoken.jwt.SaJwtTemplate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class FilmixJwtTemplate extends SaJwtTemplate {
    @Override
    public String createToken(String loginType, Object loginId, Map<String, Object> extraData, String keyt){

        String jwt = JWT.create()
                .withHeader(Map.of("type","JWT"))

                .withClaim(LOGIN_ID,loginId.toString())
                .withClaim(LOGIN_TYPE,loginType)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(keyt));
        return jwt;
    }
    @Override
    public String createToken(String loginType, Object loginId, String device,
                              long timeout, Map<String, Object> extraData, String keyt) {

        // 计算 eff 有效期：
        // 		如果 timeout 指定为 -1，那么 eff 也为 -1，代表永不过期
        // 		如果 timeout 指定为一个具体的值，那么 eff 为 13 位时间戳，代表此 token 到期的时间
        long effTime = timeout;
        if(timeout != NEVER_EXPIRE) {
            effTime = timeout * 1000 + System.currentTimeMillis();
        }

        String jwt = JWT.create()
                .withHeader(Map.of("type","JWT"))

                .withClaim(LOGIN_ID,loginId.toString())
                .withClaim(LOGIN_TYPE,loginType)
                .withClaim(DEVICE,device)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+effTime))
                .sign(Algorithm.HMAC256(keyt));

        // 创建
//        JWT jwt = JWT.create()
//                .setPayload(LOGIN_TYPE, loginType)
//                .setPayload(LOGIN_ID, loginId)
//                .setPayload(DEVICE, device)
//                .setPayload(EFF, effTime)
//                // 塞入一个随机字符串，防止同账号同一毫秒下每次生成的 token 都一样的
//                .setPayload(RN_STR, SaFoxUtil.getRandomString(32))
//                .addPayloads(extraData);
//
//        // 返回
//        return generateToken(jwt, keyt);
        return jwt;
    }
}
