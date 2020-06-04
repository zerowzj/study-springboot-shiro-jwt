package test.study.springboot.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import study.springboot.shiro.jwt.support.utils.TokenUtils;

@Slf4j
public class TokenUtilsTest {

    @Test
    public void test() {
        String name = "wzj";

        String token = TokenUtils.createToken();
        log.info("{}", token);
        token = TokenUtils.createToken();
        log.info("{}", token);
    }
}
