package pe.com.ask.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.exception.GlobalExceptionFilter;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler, GlobalExceptionFilter filter) {
        return route(POST("/api/v1/usuarios"), userHandler::listenPOSTRegisterUserUseCase)
                .filter(filter);
    }
}