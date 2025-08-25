package pe.com.ask.api.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.exception.model.ErrorResponse;
import pe.com.ask.api.exception.model.ValidationException;
import pe.com.ask.usecase.exception.BaseException;
import pe.com.ask.usecase.exception.RoleNotFoundException;
import pe.com.ask.usecase.exception.UserAlreadyExistsException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class GlobalExceptionFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    @NonNull
    public Mono<ServerResponse> filter(@NonNull ServerRequest request,
                                       @NonNull HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(ValidationException.class, ex ->
                        ServerResponse.badRequest().bodyValue(
                                ErrorResponse.builder()
                                        .tittle("Validation failed")
                                        .message(ex.getMessage())
                                        .errors(ex.getErrors())
                                        .status(400)
                                        .timestamp(LocalDateTime.now())
                                        .build()
                        )
                )
                .onErrorResume(BaseException.class, ex ->
                        ServerResponse.status(ex.getStatus()).bodyValue(
                                ErrorResponse.builder()
                                        .tittle(ex.getTitle())
                                        .message(ex.getMessage())
                                        .errors(null)
                                        .status(ex.getStatus())
                                        .timestamp(ex.getTimestamp())
                                        .build()
                        )
                )
                .onErrorResume(ex ->
                        ServerResponse.status(500).bodyValue(
                                ErrorResponse.builder()
                                        .tittle("Unexpected Error")
                                        .message(ex.getMessage())
                                        .errors(null)
                                        .status(500)
                                        .timestamp(LocalDateTime.now())
                                        .build()
                        )
                );
    }
}