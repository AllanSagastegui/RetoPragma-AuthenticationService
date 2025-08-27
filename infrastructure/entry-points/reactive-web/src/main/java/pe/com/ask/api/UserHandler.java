package pe.com.ask.api;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.TokenMapper;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.usecase.exception.BaseException;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final ValidationService validationService;

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;

    public Mono<ServerResponse> listenPOSTSignUpUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpDTO.class)
                .flatMap(validationService::validate)
                .map(userMapper::toEntity)
                .flatMap(signUpUseCase::signUpUser)
                .map(userMapper::toResponse)
                .flatMap(response
                        -> ServerResponse.created(URI.create("/api/v1/usuarios/"))
                        .bodyValue(response))
                .onErrorResume(ex -> Mono.error(
                        ex instanceof BaseException ? ex : new UnexpectedException(ex)
                ));
    }

    public Mono<ServerResponse> listenPOSTSignInUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignInDTO.class)
                .flatMap(validationService::validate)
                .flatMap(dto -> signInUseCase.signInUser(dto.email(), dto.password()))
                .map(tokenMapper::toResponse)
                .flatMap(response
                        -> ServerResponse.ok()
                        .bodyValue(response))
                .onErrorResume(ex -> Mono.error(
                        ex instanceof BaseException ? ex : new UnexpectedException(ex)
                ));
    }

    public Mono<SignUpResponse> signUpDoc(
            @RequestBody(description = "Sign Up - Data required to register a new user")
            SignUpDTO dto) {
        return Mono.empty();
    }

    public Mono<SignInResponse> signInDoc(
            @RequestBody(description = "Sign In - Credentials for user login")
            SignInDTO dto) {
        return Mono.empty();
    }
}