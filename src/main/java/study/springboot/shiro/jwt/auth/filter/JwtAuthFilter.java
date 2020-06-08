package study.springboot.shiro.jwt.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import study.springboot.shiro.jwt.auth.jwt.JwtUtils;
import study.springboot.shiro.jwt.auth.token.JwtToken;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.session.UserInfoContext;
import study.springboot.shiro.jwt.support.utils.CookieUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class JwtAuthFilter extends AccessControlFilter {

    private static String C_JWT = "jwt";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws UnauthorizedException {
        log.info(">>>>>>>>>> isAccessAllowed");
        //始终返回false，使用onAccessDenied()方法
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info(">>>>>>>>>> onAccessDenied");
        //该步骤主要是通过token代理登录shiro
        try {
            //******************** <1>.获取Jwt ********************
            HttpServletRequest request = WebUtils.toHttp(servletRequest);
            if (!CookieUtils.contain(request, C_JWT)) {
                log.error("cookie未包含登录信息");
                throw new RuntimeException("请重新登录");
            }
            String jwt = CookieUtils.getValue(request, C_JWT);
            boolean isLegal = JwtUtils.verifyJwt(jwt);
            if (!isLegal) {
                throw new RuntimeException("签名错误");
            }
            //******************** <2>.生成Token，然后代理登录和认证 ********************
            JwtToken jwtToken = new JwtToken(jwt);
            //（★）委托给Realm进行登录和授权
            Subject subject = getSubject(servletRequest, servletResponse);
            //登录
            subject.login(jwtToken);
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
