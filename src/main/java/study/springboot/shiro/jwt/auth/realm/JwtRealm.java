package study.springboot.shiro.jwt.auth.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import study.springboot.shiro.jwt.auth.jwt.JwtUtils;
import study.springboot.shiro.jwt.auth.token.JwtToken;
import study.springboot.shiro.jwt.service.popedom.PopedomService;
import study.springboot.shiro.jwt.support.redis.RedisClient;
import study.springboot.shiro.jwt.support.redis.RedisKeys;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.session.UserInfoContext;
import study.springboot.shiro.jwt.support.utils.JsonUtils;

import java.util.Map;

/**
 * 主要用于Shiro的登录认证以及权限认证
 */
@Slf4j
@Component
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private PopedomService popedomService;

    /**
     * 该Realm仅支持CustomAuthToken类型Token，其他类型处理将会抛出异常
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        log.info(">>>>>>>>>> supports");
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }

    /**
     * ====================
     * （★）获取认证信息
     * ====================
     * 每次请求的时候都会调用这个方法验证token是否失效和用户是否被锁定
     * UnknownAccountException
     * IncorrectCredentialsException
     * LockedAccountException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info(">>>>>>>>>> doGetAuthenticationInfo");
        //******************** <1>.获取jwt ********************
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String jwt = (String) jwtToken.getPrincipal();
        if (StringUtils.isEmpty(jwt)) {
            throw new UnknownAccountException("token为空");
        }

        //******************** <2>.获取token ********************
        Map<String, String> claims = JwtUtils.parseJwt(jwt);
        String token = claims.get("token");

        //******************** <3>.获取用户信息 ********************
        String key = RedisKeys.keyOfUserInfo(token);
        String text = redisClient.get(key);
        if (Strings.isNullOrEmpty(text)) {
            throw new IncorrectCredentialsException("token过期或错误");
        }
        UserInfo userInfo = JsonUtils.fromJson(text, UserInfo.class);
        UserInfoContext.set(userInfo);

        //******************** <4>.创建认证对象 ********************
        Object principal = userInfo;
        Object credentials = jwt;
        String realmName = getName();
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
        return info;
    }

    /**
     * ====================
     * （★）获取授权信息
     * ====================
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info(">>>>>>>>>> doGetAuthorizationInfo");
        return null;
    }
}
