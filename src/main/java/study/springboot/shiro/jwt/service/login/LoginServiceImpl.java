package study.springboot.shiro.jwt.service.login;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.springboot.shiro.jwt.support.redis.RedisClient;
import study.springboot.shiro.jwt.support.redis.RedisKeys;
import study.springboot.shiro.jwt.support.result.Result;
import study.springboot.shiro.jwt.support.result.Results;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.utils.JsonUtils;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Override
    public Result login(String username, String password) {
        String token = "666666666";
        //
        String key = RedisKeys.keyOfToken(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(900001L);
        redisClient.set(key, JsonUtils.toJson(userInfo));
        //
        Map<String, Object> data = Maps.newHashMap();
        data.put("token", token);
        return Results.success(data);
    }
}
