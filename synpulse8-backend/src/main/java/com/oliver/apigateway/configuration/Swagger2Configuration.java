package com.oliver.apiGateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oliver"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Setting up swagger ui.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("E-Banking Portal")
                .description("REST API of getting transactions for given account")
                .version("0.1.0")
                .build();
    }
}
