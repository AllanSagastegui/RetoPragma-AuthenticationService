package pe.com.ask.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.CreateRoleDTO;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.RoleMapper;
import pe.com.ask.usecase.createrole.CreateRoleUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RoleHandler {
    private final RoleMapper mapper;
    private final CreateRoleUseCase createRoleUseCase;
    private final ValidationService validationService;

    public Mono<ServerResponse> listenPOSTCreateRoleUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateRoleDTO.class)
                .flatMap(validationService::validate)
                .map(mapper::toEntity)
                .flatMap(createRoleUseCase::saveRole)
                .map(mapper::toResponse)
                .flatMap(response
                        -> ServerResponse.created(URI.create("/api/v1/role/")).bodyValue(response));
    }
}
