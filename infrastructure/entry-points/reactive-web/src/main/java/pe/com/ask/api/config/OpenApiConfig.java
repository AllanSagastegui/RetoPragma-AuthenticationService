package pe.com.ask.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Authentication Service API",
        version = "1.0",
        description = "This service provides secure authentication and authorization mechanisms for Crediya."
))
public class OpenApiConfig {
}