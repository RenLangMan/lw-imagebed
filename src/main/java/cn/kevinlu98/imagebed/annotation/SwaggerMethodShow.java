package cn.kevinlu98.imagebed.annotation;

import java.lang.annotation.*;

/**
 * Author: 鲁恺文
 * Date: 2020/12/29 3:50 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SwaggerMethodShow {
}
