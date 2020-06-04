package study.springboot.shiro.jwt.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class JwtUtils {

    private final static String DEFAULT_SECRET_KEY = "abc!@#XYZ";

    private final static Algorithm DEFAULT_ALGORITHM = Algorithm.HMAC256(DEFAULT_SECRET_KEY);

    /**
     * 生成jwt
     */
    public static String createJwt(Map<String, String> claims) {
        JWTCreator.Builder builder = JWT.create();
        if (claims != null) {
            claims.forEach((k, v) -> {
                builder.withClaim(k, v);
            });
        }
        String jwt = builder.sign(DEFAULT_ALGORITHM);
        return jwt;
    }

    /**
     * 解析jwt
     */
    public static Map<String, String> parseJwt(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
//        log.info("header: {}", decodedJWT.getHeader());
//        log.info("payload: {}", decodedJWT.getPayload());
//        log.info("signature: {}", decodedJWT.getSignature());
        Map<String, String> claims = Maps.newHashMap();
        decodedJWT.getClaims().forEach((k, v) -> {
            claims.put(k, v.asString());
        });
        return claims;
    }

    /**
     * 验证jwt
     */
    public static boolean verifyJwt(String jwt) {
        JWTVerifier verifier = JWT.require(DEFAULT_ALGORITHM)
                .build();
        boolean isOk = true;
        try {
            verifier.verify(jwt);
        } catch (Exception ex) {
            isOk = false;
        }
        return isOk;
    }
}
