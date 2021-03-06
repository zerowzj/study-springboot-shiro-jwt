package test.study.springboot.shiro.jwt;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import study.springboot.shiro.jwt.support.jwt.JwtUtils;

import java.util.Map;

@Slf4j
public class JwtUtilsTest {

    @Test
    public void test() {
        Map<String, String> claims = Maps.newHashMap();
        claims.put("iss", "asdfasdf");
        claims.put("username", "tom");
        claims.put("password", "123456");

        String jwt = JwtUtils.createJwt(claims);
        log.info("{}", jwt);

        log.info("合法 {}", JwtUtils.verifyJwt(jwt));
        JwtUtils.parseJwt(jwt).forEach((k, v) -> {
            log.info("{}= {}", k, v);
        });
    }
}
