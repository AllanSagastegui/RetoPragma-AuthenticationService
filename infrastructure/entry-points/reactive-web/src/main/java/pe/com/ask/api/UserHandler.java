package pe.com.ask.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.RegisterUserDTO;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.usecase.registeruser.RegisterUserUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserMapper mapper;
    private final RegisterUserUseCase registerUserUseCase;
    private final ValidationService validationService;

    public Mono<ServerResponse> listenPOSTRegisterUserUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserDTO.class)
                .flatMap(validationService::validate)
                .map(mapper::toEntity)
                .flatMap(registerUserUseCase::saveUser)
                .map(mapper::toResponse)
                .flatMap(response
                        -> ServerResponse.created(URI.create("/api/v1/usuarios/")).bodyValue(response));
    }
}