package com.yl.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.yl.common.config.BusinessException;
import com.yl.common.config.ResultCode;

import java.util.Date;

public class JWTUtil {

    private static final String USER_INFO_KEY = "user_name";
    private static final String KEY = "zhuang";

    /**
     * 生成Token
     *
     * @param username        用户标识(唯一)
     * @param tokenExpireTime 过期时间
     * @return
     */
    public static String generateToken(String username, long tokenExpireTime) {
        Algorithm algorithm = Algorithm.HMAC256(KEY);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + tokenExpireTime);
        String token = JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim(USER_INFO_KEY, username)
                .sign(algorithm);

        return token;
    }

    /**
     * 校验Token
     *
     * @param token 访问秘钥
     * @return
     */
    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
            return true;
        } catch (JWTDecodeException jwtDecodeException) {
            throw new BusinessException(ResultCode.TOKEN_INVALID.getCode(), ResultCode.TOKEN_INVALID.getMsg());
        } catch (SignatureVerificationException signatureVerificationException) {
            throw new BusinessException(ResultCode.TOKEN_SIGNATURE_INVALID.getCode(), ResultCode.TOKEN_SIGNATURE_INVALID.getMsg());
        } catch (TokenExpiredException tokenExpiredException) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED.getCode(), ResultCode.TOKEN_EXPIRED.getMsg());
        } catch (Exception ex) {
            throw new BusinessException(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMsg());
        }
    }

    /**
     * 从Token中提取用户信息
     *
     * @param token
     * @return
     */
    public static String getUserInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(USER_INFO_KEY).asString();
    }

}