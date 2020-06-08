package study.springboot.shiro.jwt.support.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class CookieUtils {

    private static final Integer DEFAULT_EXPIRY = 1 * 60;

    private CookieUtils() {
    }

    /**
     * ====================
     * 创建Cookie
     * ====================
     */
    public static Cookie newCookie(String name, String value) {
        return newCookie(name, value, DEFAULT_EXPIRY);
    }

    public static Cookie newCookie(String name, String value, int expiry) {
        return newCookie(name, value, expiry, true);
    }

    public static Cookie newCookie(String name, String value, int expiry, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(expiry);
        return cookie;
    }

    /**
     * ====================
     * 是否包含某Cookie
     * ====================
     */
    public static boolean contain(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        boolean isContain = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (cookieName.equalsIgnoreCase(name)) {
                    isContain = true;
                    break;
                }
            }
        }
        return isContain;
    }

    /**
     * ====================
     * 获取Cookie值
     * ====================
     */
    public static String getValue(HttpServletRequest request, String cookieName) {
        String value = null;
        if (contain(request, cookieName)) {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (cookieName.equalsIgnoreCase(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
}
