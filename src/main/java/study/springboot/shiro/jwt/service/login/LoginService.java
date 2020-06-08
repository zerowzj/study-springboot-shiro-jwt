package study.springboot.shiro.jwt.service.login;

import study.springboot.shiro.jwt.support.result.Result;

public interface LoginService {

    String login(String username, String password);

    Result logout();
}
