package study.springboot.shiro.jwt.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.shiro.jwt.service.login.LoginService;
import study.springboot.shiro.jwt.support.result.Result;
import study.springboot.shiro.jwt.support.result.Results;
import study.springboot.shiro.jwt.support.utils.CookieUtils;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String jwt = loginService.login(username, password);

        response.addCookie(CookieUtils.newCookie("jwt", jwt));

        return Results.success();
    }

    @PostMapping("/logout")
    public Result logout() {
        return loginService.logout();
    }
}
