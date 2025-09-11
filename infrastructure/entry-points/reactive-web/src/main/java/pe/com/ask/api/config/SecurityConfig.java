package pe.com.ask.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import pe.com.ask.api.utils.roles.Roles;
import pe.com.ask.api.utils.routes.Routes;
import pe.com.ask.security.filter.JWTFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JWTFilter jwtFilter,
            ServerAuthenticationEntryPoint authenticationEntryPoint,
            ServerAccessDeniedHandler accessDeniedHandler
    ) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers(
                                Routes.ACTUATOR,
                                Routes.SIGNIN,
                                Routes.SWAGGER_UI,
                                Routes.SWAGGER_DOCS,
                                Routes.SWAGGER_API_DOC,
                                Routes.WEBJARS,
                                Routes.TEST
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, Routes.SIGNUP).hasAnyRole(Roles.ADMIN, Roles.ADVISOR)
                        .pathMatchers(HttpMethod.POST, Routes.GETUSERSBYID).hasAnyRole(Roles.ADMIN, Roles.ADVISOR, Roles.CLIENT)
                        .anyExchange()
                        .authenticated())
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }
}