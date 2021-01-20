package cn.kevinlu98.imagebed.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Author: 鲁恺文
 * Date: 2021/1/18 7:39 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Component
public class WebsiteInfoInterceptor implements HandlerInterceptor {
    @Value("${website.title}")
    private String title;

    public static final String TITLE_CONSTANT = "website_title";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute(TITLE_CONSTANT) == null) {
            session.setAttribute(TITLE_CONSTANT, title);
        }
        return true;
    }
}
