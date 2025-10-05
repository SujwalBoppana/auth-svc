package dev.tbyte.resource.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuration for Swagger/OpenAPI documentation.
 * Defines the API info and the security scheme for JWT Bearer Authentication.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Resource Management Service API", version = "1.0"))
@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SwaggerConfig {
}