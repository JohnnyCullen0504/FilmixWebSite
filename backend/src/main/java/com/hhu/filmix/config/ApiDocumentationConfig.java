package com.hhu.filmix.config;

import com.hhu.filmix.api.ApiCode;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@SecurityScheme(
        name = "authToken",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"

)
public class ApiDocumentationConfig {
    @Bean
    public OpenAPI rotaOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Flimix Api文档")
                        .description("影院购票APP后端服务的接口文档")
                        .version("v0.0.1")
                        .contact(new Contact().name("Max")))
                .externalDocs(new ExternalDocumentation());

    }
    @Bean
    public OpenApiCustomizer openApiCustomizer(){
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        ApiResponses apiResponses = operation.getResponses();
                        Arrays.stream(ApiCode.values()).forEach(apiCode -> {
                            apiResponses.addApiResponse(
                                    apiCode.value().toString(),
                                    new ApiResponse().description(apiCode.description()));
                        });

                    }));
        };
    }
}
