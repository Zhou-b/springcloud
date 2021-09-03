package com.liu.spring.utils.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2018-04-18
 * Time: 14:52
 */
@EnableWebMvc
@EnableSwagger2
@Configuration
public class Swagger2Config {
    private static String PROJECT_NAME = "平台";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.liu"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(PROJECT_NAME + "    文档v1.1.0")
                .description(PROJECT_NAME + "    接口说明文档")
                .termsOfServiceUrl("")
                .contact(new Contact("浪","lang.com","@qq.com"))
                .version("1.0")
                .build();
    }
}
