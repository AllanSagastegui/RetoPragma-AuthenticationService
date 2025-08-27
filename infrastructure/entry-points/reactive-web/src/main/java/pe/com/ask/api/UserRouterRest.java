package pe.com.ask.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.api.exception.GlobalExceptionFilter;
import pe.com.ask.api.utils.routes.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "signUpDoc",
                    operation = @Operation(
                            summary = "Register a new user",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = SignUpDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User created successfully",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SignUpResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validation Errors"
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "User Already Exists"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "signInDoc",
                    operation = @Operation(
                            summary = "Login user",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = SignInDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login successful",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SignInResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validation Errors"
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Invalid credentials"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler, GlobalExceptionFilter filter) {
        return route(POST(Routes.SIGNUP), userHandler::listenPOSTSignUpUseCase)
                .andRoute(POST(Routes.SIGNIN), userHandler::listenPOSTSignInUseCase)
                .filter(filter);
    }
}