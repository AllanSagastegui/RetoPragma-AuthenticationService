package pe.com.ask.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.utils.logmessages.SignInLog;
import pe.com.ask.api.utils.logmessages.SignUpLog;
import pe.com.ask.api.mapper.TokenMapper;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final ValidationService validationService;
    private final CustomLogger logger;

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;

    public Mono<ServerResponse> listenPOSTSignUpUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpDTO.class)
                .doOnSubscribe(sub -> logger.trace(SignUpLog.SIGNUP_START))
                .doOnNext(dto -> logger.trace(SignUpLog.SIGNUP_RECEIVED_BODY))
                .flatMap(validationService::validate)
                .doOnNext(validDto -> logger.trace(SignUpLog.SIGNUP_VALIDATION_SUCCESS, validDto.email()))
                .map(userMapper::toEntity)
                .doOnNext(entity -> logger.trace(SignUpLog.SIGNUP_MAPPED_DTO_TO_ENTITY))
                .flatMap(signUpUseCase::signUpUser)
                .doOnNext(savedUser -> logger.trace(SignUpLog.SIGNUP_USER_SAVED, savedUser.getId()))
                .map(userMapper::toResponse)
                .doOnNext(response -> logger.trace(SignUpLog.SIGNUP_MAPPED_ENTITY_TO_RESPONSE, response))
                .flatMap(response
                        -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response))
                .doOnSuccess(resp -> logger.trace(SignUpLog.SIGNUP_RESPONSE_CREATED))
                .onErrorResume(ex -> {
                    logger.trace(SignUpLog.SIGNUP_ERROR, ex.getMessage());
                    return Mono.error(
                            ex instanceof BaseException ? ex : new UnexpectedException(ex)
                    );
                });
    }

    public Mono<ServerResponse> listenPOSTSignInUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignInDTO.class)
                .doOnSubscribe(sub -> logger.trace(SignInLog.SIGNIN_START))
                .doOnNext(dto -> logger.trace(SignInLog.SIGNIN_RECEIVED_BODY))
                .flatMap(validationService::validate)
                .doOnNext(validDto -> logger.trace(SignInLog.SIGNIN_VALIDATION_SUCCESS, validDto.email()))
                .flatMap(dto -> {
                    logger.trace(SignInLog.SIGNIN_ATTEMPT, dto.email());
                    return signInUseCase.signInUser(dto.email(), dto.password());
                })
                .doOnNext(token -> logger.trace(SignInLog.SIGNIN_SUCCESS))
                .map(tokenMapper::toResponse)
                .flatMap(response
                        -> ServerResponse
                        .status(HttpStatus.OK)
                        .bodyValue(response))
                .doOnSuccess(resp -> logger.trace(SignInLog.SIGNIN_RESPONSE_CREATED))
                .onErrorResume(ex -> {
                    logger.trace(SignInLog.SIGNIN_ERROR, ex.getMessage());
                    return Mono.error(
                            ex instanceof BaseException ? ex : new UnexpectedException(ex)
                    );
                });
    }
}