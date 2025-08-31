package pe.com.ask.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.doc.SignInDoc;
import pe.com.ask.api.doc.SignUpDoc;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = SignUpDoc.class,
                    beanMethod = "signUpDoc"
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = SignInDoc.class,
                    beanMethod = "signInDoc"
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return route(POST(Routes.SIGNUP).and(accept(MediaType.APPLICATION_JSON)), userHandler::listenPOSTSignUpUseCase)
                .andRoute(POST(Routes.SIGNIN).and(accept(MediaType.APPLICATION_JSON)), userHandler::listenPOSTSignInUseCase);
    }
}