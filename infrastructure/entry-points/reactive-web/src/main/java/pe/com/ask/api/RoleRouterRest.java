package pe.com.ask.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.exception.GlobalExceptionFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoleRouterRest {
    @Bean
    public RouterFunction<ServerResponse> roleRouterFunction(RoleHandler roleHandler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/role"), roleHandler::listenPOSTCreateRoleUseCase)
                .filter(filter);
    }
}
