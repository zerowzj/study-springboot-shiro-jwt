package study.springboot.shiro.jwt.auth.subject;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.stereotype.Component;

/**
 * 重写DefaultWebSubjectFactory，主要是关闭创建Session
 */
@Slf4j
@Component
public class JwtSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建Session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
