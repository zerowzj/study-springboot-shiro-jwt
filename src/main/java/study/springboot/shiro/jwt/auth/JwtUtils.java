package study.springboot.shiro.jwt.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
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
    public static String createToken(Map<String, String> claims) {
        JWTCreator.Builder builder = JWT.create();
        if (claims != null) {
            claims.forEach((k, v) -> {
                builder.withClaim(k, v);
            });
        }
        String token = builder.sign(DEFAULT_ALGORITHM);
        return token;
    }

    /**
     * 解析jwt
     */
    public static Map<String, Claim> parseToken(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);

        log.info("header: {}", decodedJWT.getHeader());
        log.info("payload: {}", decodedJWT.getPayload());
        log.info("signature: {}", decodedJWT.getSignature());

        return decodedJWT.getClaims();
    }

    /**
     * 验证jwt
     */
    public static boolean verify(String jwt) {
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

    public static void main(String[] args) {
        Map<String, String> claims = Maps.newHashMap();
        claims.put("username", "tom");
        claims.put("password", "123456");

        String jwtToken = createToken(claims);
        log.info("{}", jwtToken);

        log.info("合法="+verify(jwtToken));
        parseToken(jwtToken).forEach((k, v) ->{
            log.info("{}= {}", k, v.asString());
        });
    }
}
