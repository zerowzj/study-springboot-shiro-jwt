package study.springboot.shiro.jwt.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import study.springboot.shiro.jwt.auth.filter.JwtAuthFilter;

@Component
public class ShiroFilterCfg {

    @Bean
    public FilterRegistrationBean customAuthFilter(JwtAuthFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
