package study.springboot.shiro.jwt.support.session;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserInfo implements Serializable {

    private Long userId;
}
