package com.oliver.apiGateway.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.oliver.response.StatusCode;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Sets.*;

@Component
public class SwaggerAPIList implements ApiListingScannerPlugin {
    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        ApiDescription loginApi = createLoginApi();
        ApiDescription logoutApi = createLogoutApi();

        return Arrays.asList(loginApi, logoutApi);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }

    /**
     * Create login api swagger documentation.
     */
    private ApiDescription createLoginApi() {
        Operation loginApi = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("User Login Api")
                .consumes(newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(newHashSet("Login"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("UserForm")
                                .type(new TypeResolver().resolve(String.class))
                                .name("User login form")
                                .parameterType("body")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("UserForm"))
                                .build()
                ))
                .responseMessages(
                        newHashSet(
                                new ResponseMessageBuilder().code(200)
                                        .message("Log-in successfully")
                                        .responseModel(new ModelRef("ResponseResult"))
                                        .build()
                        )

                )
                .build();
        return new ApiDescription("/login", "User Login API",
                Arrays.asList(loginApi), false);
    }

    /**
     * Create logout API documentation
     */
    private ApiDescription createLogoutApi() {
        Operation logoutApi = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("User Logout Api")
                .tags(newHashSet("Logout"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("JWT token with \"Bearer\" prefix")
                                .type(new TypeResolver().resolve(String.class))
                                .name("Authorization")
                                .parameterType("header")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(
                        newHashSet(
                                new ResponseMessageBuilder().code(200)
                                        .message("Logout successfully")
                                        .responseModel(new ModelRef("ResponseResult"))
                                        .build()
                        )
                )
                .build();
        return new ApiDescription("/logout", "User Logout API",
                Arrays.asList(logoutApi), false);
    }
}
