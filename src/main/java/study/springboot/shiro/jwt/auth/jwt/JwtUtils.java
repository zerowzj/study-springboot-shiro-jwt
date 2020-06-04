package study.springboot.shiro.jwt.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    public static String createToken(String userId, long ttlMs) {
        //签发时间（iat）
        long nowMs = System.currentTimeMillis();
        Date now = new Date(nowMs);

        //payload标准声明和私有声明
        JwtBuilder builder = Jwts.builder()
                .setId("id")       //编号（jti）
                .setIssuer(userId) //签发人（iss）
                .setIssuedAt(now)  //签发时间（iat）
//                .setSubject("")    //主题，（sub）
//                .setAudience("")
                //生成签名的算法和秘钥
                .signWith(ALGORITHM, "123132");

        if (ttlMs >= 0) {
            long expMillis = nowMs + ttlMs;
            Date exp = new Date(expMillis);
            //过期时间（exp）
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims decode(String jwt) {
        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey("123132")
                // 设置需要解析的 jwt
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "tom");
        map.put("password", "123456");
        map.put("age", 20);

        String jwtToken = createToken("tom", 10 * 1000);

        System.out.println(jwtToken);
        /*
        util.isVerify(jwtToken);
        System.out.println("合法");
        */
        decode(jwtToken).entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });

    }
}
