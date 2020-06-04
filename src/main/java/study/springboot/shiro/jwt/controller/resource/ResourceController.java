package study.springboot.shiro.jwt.controller.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springboot.shiro.jwt.support.result.Result;
import study.springboot.shiro.jwt.support.result.Results;
import study.springboot.shiro.jwt.support.session.UserInfo;
import study.springboot.shiro.jwt.support.session.UserInfoContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/res")
public class ResourceController {

    private static ExecutorService POOL = Executors.newCachedThreadPool();

    @PostMapping("/list")
    public Result list() {
        POOL.execute(() -> {
            UserInfo userInfo = UserInfoContext.get();
            log.info("===>{}", userInfo.getUserId());
        });

//        Thread t = new Thread(() -> {
//            UserInfo userInfo = UserInfoContext.get();
//            log.info("===>{}", userInfo.getUserId());
//        });
//        t.start();
        return Results.success();
    }

    @PostMapping("/add")
    public Result add() {
        return Results.success();
    }

    @PostMapping("/modify")
    public Result modify() {
        return Results.success();
    }
}
