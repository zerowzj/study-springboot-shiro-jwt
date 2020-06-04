package study.springboot.shiro.jwt.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;

public class JwtUtils {

    private final static SignatureAlgorithm DEFAULT_ALGORITHM = SignatureAlgorithm.HS256;

    private final static String DEFAULT_SECRET_KEY = "abc!@#XYZ";

    public static String createToken(Claims claims) {
        return createToken(claims, null, null);
    }

    public static String createToken(Claims claims, SignatureAlgorithm algorithm, String base64SecretKey) {
        if (algorithm == null) {
            algorithm = DEFAULT_ALGORITHM;
        }
        if (base64SecretKey == null) {
            base64SecretKey = DEFAULT_SECRET_KEY;
        }
        //payload标准声明和私有声明
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(algorithm, base64SecretKey);
        return builder.compact();
    }

    public static Claims parseToken(String jwt) {
        return parseToken(jwt, null);
    }

    public static Claims parseToken(String jwt, String base64SecretKey) {
        if (base64SecretKey == null) {
            base64SecretKey = DEFAULT_SECRET_KEY;
        }
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(base64SecretKey)
                .parseClaimsJws(jwt);

        return jws.getBody();
    }

//    public boolean verify(String jwt) {
//        Algorithm algorithm;
//
//        switch (signatureAlgorithm) {
//            case HS256:
//                algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
//                break;
//            default:
//                throw new RuntimeException("不支持该算法");
//        }
//
//        JWTVerifier verifier = JWT.require(algorithm)
//                .build();
//        try {
//            verifier.verify(jwt);
//        } catch (Exception ex) {
//
//        }
//        /*
//            // 得到DefaultJwtParser
//            Claims claims = decode(jwtToken);
//
//            if (claims.get("password").equals(user.get("password"))) {
//                return true;
//            }
//        */
//        return true;
//    }

    public static void main(String[] args) {
        Claims claims = new DefaultClaims();
        claims.setId("123123");
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
