package pe.com.ask.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.TokenMapper;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.model.user.User;
import pe.com.ask.model.token.Token;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserHandlerTest {

    @Mock private UserMapper userMapper;
    @Mock private TokenMapper tokenMapper;
    @Mock private ValidationService validationService;
    @Mock private SignUpUseCase signUpUseCase;
    @Mock private SignInUseCase signInUseCase;
    @Mock private ServerRequest serverRequest;

    private UserHandler userHandler;

    private User userEntity;
    private Token tokenEntity;
    private SignUpDTO signUpDTO;
    private SignInDTO signInDTO;
    private SignUpResponse signUpResponse;
    private SignInResponse signInResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new User();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName("Allan");
        userEntity.setLastName("Sagastegui");
        userEntity.setDni("12345678");
        userEntity.setEmail("allan.sagastegui@test.com");
        userEntity.setPassword("password123");
        userEntity.setBirthday(LocalDate.parse("2003-03-11"));
        userEntity.setAddress("Mi Casa, Lima, Peru");
        userEntity.setPhone("987654321");
        userEntity.setBaseSalary(new BigDecimal("2500.50"));

        tokenEntity = new Token();
        tokenEntity.setToken("token-example");

        signUpDTO = new SignUpDTO(
                userEntity.getName(),
                userEntity.getLastName(),
                userEntity.getDni(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getBirthday().toString(),
                userEntity.getAddress(),
                userEntity.getPhone(),
                userEntity.getBaseSalary()
        );

        signInDTO = new SignInDTO(userEntity.getEmail(), userEntity.getPassword());

        signUpResponse = new SignUpResponse(
                userEntity.getName(),
                userEntity.getLastName(),
                userEntity.getDni(),
                userEntity.getEmail(),
                userEntity.getBirthday().toString(),
                userEntity.getAddress(),
                userEntity.getPhone(),
                userEntity.getBaseSalary()
        );

        signInResponse = new SignInResponse(tokenEntity.getToken());

        when(validationService.validate(any(SignUpDTO.class))).thenReturn(Mono.just(signUpDTO));
        when(validationService.validate(any(SignInDTO.class))).thenReturn(Mono.just(signInDTO));

        when(userMapper.toEntity(any(SignUpDTO.class))).thenReturn(userEntity);
        when(userMapper.toResponse(any(User.class))).thenReturn(signUpResponse);

        when(signUpUseCase.signUpUser(any(User.class))).thenReturn(Mono.just(userEntity));

        when(signInUseCase.signInUser(anyString(), anyString()))
                .thenReturn(Mono.just(tokenEntity));
        when(tokenMapper.toResponse(any(Token.class)))
                .thenReturn(signInResponse);

        userHandler = new UserHandler(userMapper, tokenMapper, validationService, signUpUseCase, signInUseCase);
    }

    @Test
    void testListenPOSTSignUpUseCase() {
        when(serverRequest.bodyToMono(SignUpDTO.class)).thenReturn(Mono.just(signUpDTO));

        Mono<ServerResponse> result = userHandler.listenPOSTSignUpUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(res -> res instanceof ServerResponse)
                .verifyComplete();

        verify(signUpUseCase, times(1)).signUpUser(userEntity);
    }

    @Test
    void testListenPOSTSignInUseCase() {
        when(serverRequest.bodyToMono(SignInDTO.class)).thenReturn(Mono.just(signInDTO));

        Mono<ServerResponse> result = userHandler.listenPOSTSignInUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(res -> res instanceof ServerResponse)
                .verifyComplete();

        verify(signInUseCase, times(1)).signInUser(signInDTO.email(), signInDTO.password());
    }

    @Test
    void testSignUpDocReturnsEmpty() {
        StepVerifier.create(userHandler.signUpDoc(signUpDTO))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testSignInDocReturnsEmpty() {
        StepVerifier.create(userHandler.signInDoc(signInDTO))
                .expectNextCount(0)
                .verifyComplete();
    }
}