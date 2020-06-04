package study.springboot.shiro.jwt.support.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class TokenUtils {

    public static String createToken() {
        EthernetAddress ethernetAddress = EthernetAddress.fromInterface();
        TimeBasedGenerator generator = Generators.timeBasedGenerator(ethernetAddress);
        return generator.generate().toString();
    }

    public static String createToken(String name) {
        NameBasedGenerator generator = Generators.nameBasedGenerator();
        return generator.generate(name).toString();
    }
}
