package study.springboot.shiro.jwt.controller.login;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.shiro.jwt.service.login.LoginService;
import study.springboot.shiro.jwt.support.result.Result;
import study.springboot.shiro.jwt.support.result.Results;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //
        String jwt = loginService.login(username, password);
        Map<String, Object> data = Maps.newHashMap();
        data.put("jwt", jwt);
        //
        return Results.success(data);
    }

    @PostMapping("/logout")
    public Result logout() {
        return loginService.logout();
    }
}
