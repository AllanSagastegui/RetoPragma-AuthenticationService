package pe.com.ask.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Authentication Service API",
                version = "1.2",
                description = "The Authentication Service provides robust authentication and authorization " +
                        "mechanisms for the Crediya ecosystem. " +
                        "It supports user registration, login, JWT-based token management, and role-based access control. " +
                        "Use this API to securely authenticate users and integrate identity management into your applications.",
                contact = @Contact(
                        name = "Allan Sagastegui",
                        email = "sagasteguiherradaa@gmail.com",
                        url = "https://github.com/AllanSagastegui/RetoPragma-AuthenticationService"
                )
    ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}