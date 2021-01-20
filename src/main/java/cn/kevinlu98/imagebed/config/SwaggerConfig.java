package cn.kevinlu98.imagebed.config;

import cn.kevinlu98.imagebed.annotation.SwaggerMethodShow;
import cn.kevinlu98.imagebed.annotation.SwaggerShow;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author: 鲁恺文
 * Date: 2021/1/15 4:18 下午
 * Email: lukaiwen@xiaomi.com
 * Description:
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean swaggerEnable;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(SwaggerShow.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(SwaggerMethodShow.class))
                .paths(PathSelectors.any())
                .build().enable(swaggerEnable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("冷文图床调试")
                .description("此文档冷文图床调试说明及测试工具")
                .version("1.0")
                .build();
    }
}
