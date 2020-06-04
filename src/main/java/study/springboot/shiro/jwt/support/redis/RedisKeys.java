package study.springboot.shiro.jwt.support.redis;

import com.google.common.base.Joiner;

public class RedisKeys {

    private static final String SEPARATOR = ":";

    private static final String APP = "shiro:jwt";

    private static String format(String... key) {
        return Joiner.on(SEPARATOR).skipNulls().join(key);
    }

    public static String keyOfToken(String token) {
        return format(APP, "token", token);
    }
}
