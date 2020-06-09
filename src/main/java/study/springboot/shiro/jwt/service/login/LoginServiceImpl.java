package study.springboot.shiro.jwt.service.login;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
import study.springboot.shiro.jwt.support.utils.TokenGenerator;

import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public String login(String username, String password) {
        //******************** <1>.获取用户信息 ********************
        Long userId = 900001L;

        //******************** <2>.生成token ********************
        String token = TokenGenerator.createToken();

        //******************** <3>.存储用户信息 ********************
        String key = RedisKeys.keyOfUserInfo(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setToken(token);
        userInfo.setUserId(userId);
        redisClient.set(key, JsonUtils.toJson(userInfo));

        //******************** <4>.生成jwt ********************
        Map<String, String> claims = Maps.newHashMap();
        claims.put("userId", String.valueOf(userId));
        claims.put("token", token);
        String jwt = JwtUtils.createJwt(claims);

        return jwt;
    }

    @Override
    public Result logout() {
        String token = UserInfoContext.get().getToken();
        log.info("===> {}", token);
        String key = RedisKeys.keyOfUserInfo(token);
        redisClient.delete(key);
        //
        SecurityUtils.getSubject().logout();
        return Results.success();
    }
}
