package study.springboot.shiro.jwt.service.popedom;

import java.util.List;

public interface PopedomService {

    List<String> getFunctionLt();

    List<String> getFunctionLt(Long userId);
}
