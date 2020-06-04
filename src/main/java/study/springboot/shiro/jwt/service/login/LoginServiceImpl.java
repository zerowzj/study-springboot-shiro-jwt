package study.springboot.shiro.jwt.service.login;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.springboot.shiro.jwt.auth.jwt.JwtUtils;
import study.springboot.shiro.jwt.support.redis.RedisClient;
import study.springboot.shiro.jwt.support.redis.RedisKeys;
import study.springboot.shiro.jwt.support.result.Result;
import study.springboot.shiro.jwt.support.result.Results;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.session.UserInfoContext;
import study.springboot.shiro.jwt.support.utils.JsonUtils;

import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public Result login(String username, String password) {
        Long userId = 900001L;
        String token = "666666666";
        Map<String, String> claims = Maps.newHashMap();
        claims.put("userId", String.valueOf(userId));
        claims.put("token", token);

        //生成jwt
        String jwt = JwtUtils.createJwt(claims);
        //
        String key = RedisKeys.keyOfToken(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setToken(token);
        userInfo.setUserId(userId);
        redisClient.set(key, JsonUtils.toJson(userInfo));
        //
        Map<String, Object> data = Maps.newHashMap();
        data.put("jwt", jwt);
        return Results.success(data);
    }

    @Override
    public Result logout() {
        String token = UserInfoContext.get().getToken();
        log.info("===> {}", token);
        String key = RedisKeys.keyOfToken(token);
        redisClient.delete(key);
        return Results.success();
    }
}
