package org.planner.goalplanner.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Goal Planner API")
                        .version("1.0")
                        .description("API with JWT Authentication"))
                // Добавляем JWT security scheme
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))  // <- Это создаст кнопку Authorize
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token. Example: 'eyJhbGciOiJIUzI1...'")
                        )
                );
    }
}
