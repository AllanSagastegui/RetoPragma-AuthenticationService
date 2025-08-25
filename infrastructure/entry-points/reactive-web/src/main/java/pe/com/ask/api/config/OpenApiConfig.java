package pe.com.ask.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Authentication-Service",
        version = "1.0",
        description = "Servicio de Atuh para Crediya"
))
public class OpenApiConfig {
}