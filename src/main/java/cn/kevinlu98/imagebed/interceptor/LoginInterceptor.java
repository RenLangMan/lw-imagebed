package cn.kevinlu98.imagebed.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: 鲁恺文
 * Date: 2021/1/20 2:34 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${user.username}")
    private String username;

    public static final String USER_SIGN = "userlogin";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = (String) request.getSession().getAttribute(USER_SIGN);
        if (this.username.equals(username)) {
            return true;
        } else {
            request.setAttribute("error", "请登录后再进行操作");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        }
    }
}
