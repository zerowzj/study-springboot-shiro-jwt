package study.springboot.shiro.jwt.service.login;

import study.springboot.shiro.jwt.support.result.Result;

public interface LoginService {

    Result login(String username, String password);
}
