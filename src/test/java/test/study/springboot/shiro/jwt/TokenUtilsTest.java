package test.study.springboot.shiro.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import study.springboot.shiro.jwt.support.utils.TokenGenerator;

@Slf4j
public class TokenUtilsTest {

    @Test
    public void test() {
        String name = "wzj";

        String token = TokenGenerator.createToken();
        log.info("{}", token);
        token = TokenGenerator.createToken();
        log.info("{}", token);
    }
}
