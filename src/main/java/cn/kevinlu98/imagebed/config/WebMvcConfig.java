package cn.kevinlu98.imagebed.config;

import cn.kevinlu98.imagebed.interceptor.LoginInterceptor;
import cn.kevinlu98.imagebed.interceptor.WebsiteInfoInterceptor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 4:01 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebsiteInfoInterceptor websiteInfoInterceptor;

    private final LoginInterceptor loginInterceptor;

    public WebMvcConfig(WebsiteInfoInterceptor websiteInfoInterceptor, LoginInterceptor loginInterceptor) {
        this.websiteInfoInterceptor = websiteInfoInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(websiteInfoInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/image/login")
                .excludePathPatterns("/logout");
    }
}
