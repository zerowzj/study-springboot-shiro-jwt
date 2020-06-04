package study.springboot.shiro.jwt.auth;

import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;

public class JwtUtils {

    private static SignatureAlgorithm DEFAULT_ALGORITHM = SignatureAlgorithm.HS256;

    private static String DEFAULT_KEY = "123123";

    public static String createToken(Claims claims) {
        return createToken(claims, 0, null, null);
    }

    public static String createToken(Claims claims, long ttl,
                                     SignatureAlgorithm algorithm, String base64SecretKey) {
        //payload标准声明和私有声明
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims);
        if (algorithm == null || base64SecretKey == null) {
            builder.signWith(DEFAULT_ALGORITHM, DEFAULT_KEY);
        } else {
            builder.signWith(algorithm, base64SecretKey);
        }
        //
        if (ttl > 0) {
            long expMillis = System.currentTimeMillis() + ttl;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseToken(String jwt) {
        //得到DefaultJwtParser
        return Jwts.parser()
                //签名秘钥
                .setSigningKey(DEFAULT_KEY)
                //需要解析的jwt
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) {
        Claims claims = new DefaultClaims();
        claims.put("username", "tom");
        claims.put("password", "123456");

        String jwtToken = createToken(claims);

        System.out.println(jwtToken);
        /*
        util.isVerify(jwtToken);
        System.out.println("合法");
        */
        parseToken(jwtToken).entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
    }
}
