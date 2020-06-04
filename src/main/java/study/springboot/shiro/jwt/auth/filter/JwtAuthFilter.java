package study.springboot.shiro.jwt.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import study.springboot.shiro.jwt.auth.token.JwtToken;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.session.UserInfoContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtAuthFilter extends BasicHttpAuthenticationFilter {

    private static String AUTHORIZATION = "Authorization";

    /**
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws UnauthorizedException {
        log.info(">>>>>>>>>> isAccessAllowed");
        //始终返回false来，使用onAccessDenied()方法
        return false;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        return super.executeLogin(request, response);
    }

    /**
     * 表示当访问拒绝时是否已经处理了
     *
     * @return true表示需要继续处理；false表示该拦截器实例已经处理了，将直接返回即可
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.info(">>>>>>>>>> onAccessDenied");
        //******************** 该步骤主要是通过token代理登录shiro ********************
        //获取token值
        String jwt = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //生成AuthenticationToken，然后代理登录和认证
        JwtToken jwtAuthToken = new JwtToken(jwt);
        try {
            //（★）委托给Realm进行登录和授权验证
            Subject subject = getSubject(request, response);
            //登录
            subject.login(jwtAuthToken);
        } catch (Exception ex) {
            //log.error(ex.getLocalizedMessage(), ex);
            //登录失败不用处理后面的过滤器会处理并且能通过@ControllerAdvice统一处理相关异常
            throw ex;
        }
        return true;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response,
                                Exception exception) throws Exception {
        log.info(">>>>>> afterCompletion");
        UserInfo userInfo = UserInfoContext.get();
        if (userInfo != null) {
            log.info("remove user info context");
            UserInfoContext.remove();
        }
    }
}
