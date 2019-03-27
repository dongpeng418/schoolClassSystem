package cn.com.school.classinfo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JWTUtil {

    /**
     * 普通登录过期时间30分钟
     */
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    /**
     * token过期后可以刷新token的时间，默认30分钟，token过期超过30分钟则不能重新生成token
     */
    private static final long REFRESH_EXPIRE_TIME_SECONDS = 30 * 60 * 1000;

    /**
     * 记住我登录过期时间30天
     */
    private static final long REMEMBER_ME_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * 校验token是否过期
     *
     * @param token  密钥
     * @return 是否过期
     */
    public static boolean isExpire(String token) {
        boolean expire = false;
        Date now = DateUtil.now();
        DecodedJWT jwt = JWT.decode(token);
        Date date = jwt.getExpiresAt();
        if(Objects.nonNull(date)){
            expire = date.compareTo(now) < 0;
        }
        return expire;
    }

    /**
     * 根据过期的token重新生成新的token
     * @param token 过期的token
     * @return 新token
     */
    public static String regenerateToken(String token, String secret){
        DecodedJWT jwt = JWT.decode(token);
        //token过期时间是否超过给定的时间，如果超过则不能重新生成token
        Date now = DateUtil.now();
        long diff = now.getTime() - jwt.getExpiresAt().getTime();
        boolean disable = REFRESH_EXPIRE_TIME_SECONDS - diff < 0;
        if(disable){
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTCreator.Builder builder = JWT.create();
            //复制payload内容
            Map<String, Claim> claims = jwt.getClaims();
            if(MapUtils.isNotEmpty(claims)){
                claims.forEach((key, value) -> builder.withClaim(key, value.asString()));
            }
            //设置过期时间
            Date date = DateUtils.addSeconds(DateUtil.now(), (int) EXPIRE_TIME);
            return builder.withExpiresAt(date).sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUserType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userType").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getCustomerCompanyId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("customerCompanyId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getTenantId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("tenantId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret, long expireTime) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireTime);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret, long expireTime,int userType,int customerCompanyId) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireTime);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    .withClaim("userType", String.valueOf(userType))
                    .withClaim("customerCompanyId", String.valueOf(customerCompanyId))
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret, long expireTime,int userType,int customerCompanyId,int tenantId) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireTime);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    .withClaim("userType", String.valueOf(userType))
                    .withClaim("customerCompanyId", String.valueOf(customerCompanyId))
                    .withClaim("tenantId", String.valueOf(tenantId))
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 普通登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String normalSign(String username, String secret) {
        return sign(username, secret, EXPIRE_TIME);
    }

    /**
     * 普通登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String normalSignByUserType(String username, String secret, int userType, int customerCompanyId) {
        return sign(username, secret, EXPIRE_TIME,userType,customerCompanyId);
    }

    /**
     * 普通登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String normalSignByUserTypeAndTenantId(String username, String secret, int userType, int customerCompanyId, int tenantId) {
        return sign(username, secret, EXPIRE_TIME,userType,customerCompanyId,tenantId);
    }



    /**
     * remember me登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String rememberMeSign(String username, String secret) {
        return sign(username, secret, REMEMBER_ME_EXPIRE_TIME);
    }

    /**
     * remember me登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String rememberMeSignByUserType(String username, String secret, int userType, int customerCompanyId) {
        return sign(username, secret, REMEMBER_ME_EXPIRE_TIME,userType,customerCompanyId);
    }

    /**
     * remember me登录
     *
     * @param username 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String rememberMeSignByUserTypeAndTenantId(String username, String secret, int userType, int customerCompanyId, int tenantId) {
        return sign(username, secret, REMEMBER_ME_EXPIRE_TIME,userType,customerCompanyId,tenantId);
    }
}
