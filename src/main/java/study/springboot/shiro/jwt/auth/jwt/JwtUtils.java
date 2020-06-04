package study.springboot.shiro.jwt.auth.jwt;

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

    private final static String DEFAULT_SECRET_KEY = "abc!@#XYZ123";

    private final static SignAlg DEFAULT_ALGORITHM = SignAlg.HMAC256;

    /**
     * 生成jwt
     */
    public static String createJwt(Map<String, String> claims) {
        return createJwt(claims, null, null);
    }

    /**
     * 生成jwt
     */
    public static String createJwt(Map<String, String> claims, SignAlg signAlg, String secretKey) {
        Algorithm algorithm = transform(signAlg, secretKey);
        JWTCreator.Builder builder = JWT.create();
        if (claims != null) {
            claims.forEach((k, v) -> {
                builder.withClaim(k, v);
            });
        }
        String jwt = builder.sign(algorithm);
        return jwt;
    }

    /**
     * 验证jwt
     */
    public static boolean verifyJwt(String jwt) {
        return verifyJwt(jwt, null, null);
    }

    /**
     * 验证jwt
     */
    public static boolean verifyJwt(String jwt, SignAlg signAlg, String secretKey) {
        Algorithm algorithm = transform(signAlg, secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        boolean isOk = true;
        try {
            verifier.verify(jwt);
        } catch (Exception ex) {
            isOk = false;
        }
        return isOk;
    }

    /**
     * 解析jwt
     */
    public static Map<String, String> parseJwt(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        Map<String, String> claims = Maps.newHashMap();
        decodedJWT.getClaims().forEach((k, v) -> {
            claims.put(k, v.asString());
        });
        return claims;
    }

    private static Algorithm transform(SignAlg signAlg, String secretKey) {
        if (signAlg == null) {
            signAlg = DEFAULT_ALGORITHM;
        }
        if (secretKey == null) {
            secretKey = DEFAULT_SECRET_KEY;
        }
        Algorithm algorithm;
        switch (signAlg) {
            case HMAC256:
                algorithm = Algorithm.HMAC256(secretKey);
                break;
            case HMAC512:
                algorithm = Algorithm.HMAC512(secretKey);
                break;
            default:
                throw new RuntimeException("不支持的算法");
        }
        return algorithm;
    }
}
