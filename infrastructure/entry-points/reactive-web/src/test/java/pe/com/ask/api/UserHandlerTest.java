package pe.com.ask.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.GetAllClientsResponse;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.api.exception.model.UnexpectedException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.TokenMapper;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.user.User;
import pe.com.ask.model.token.Token;
import pe.com.ask.usecase.getusersbyid.GetUsersByIdUseCase;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    @Mock private UserMapper userMapper;
    @Mock private TokenMapper tokenMapper;
    @Mock private ValidationService validationService;
    @Mock private SignUpUseCase signUpUseCase;
    @Mock private SignInUseCase signInUseCase;
    @Mock private GetUsersByIdUseCase getUsersByIdUseCase;
    @Mock private ServerRequest serverRequest;
    @Mock private CustomLogger customLogger;

    private UserHandler userHandler;

    private User userEntity;
    private SignUpDTO signUpDTO;
    private SignInDTO signInDTO;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

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

        Token tokenEntity = new Token();
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

        SignUpResponse signUpResponse = new SignUpResponse(
                userEntity.getName(),
                userEntity.getLastName(),
                userEntity.getDni(),
                userEntity.getEmail(),
                userEntity.getBirthday().toString(),
                userEntity.getAddress(),
                userEntity.getPhone(),
                userEntity.getBaseSalary()
        );

        SignInResponse signInResponse = new SignInResponse(tokenEntity.getToken());

        when(validationService.validate(any(SignUpDTO.class))).thenReturn(Mono.just(signUpDTO));
        when(validationService.validate(any(SignInDTO.class))).thenReturn(Mono.just(signInDTO));

        when(userMapper.toEntity(any(SignUpDTO.class))).thenReturn(userEntity);
        when(userMapper.toResponse(any(User.class))).thenReturn(signUpResponse);

        when(signUpUseCase.signUpUser(any(User.class))).thenReturn(Mono.just(userEntity));

        when(signInUseCase.signInUser(anyString(), anyString()))
                .thenReturn(Mono.just(tokenEntity));
        when(tokenMapper.toResponse(any(Token.class)))
                .thenReturn(signInResponse);

        userHandler = new UserHandler(userMapper, tokenMapper, validationService, customLogger, signUpUseCase, signInUseCase, getUsersByIdUseCase);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should handle POST /usuarios request and return ServerResponse")
    void testListenPOSTSignUpUseCase() {
        when(serverRequest.bodyToMono(SignUpDTO.class)).thenReturn(Mono.just(signUpDTO));

        Mono<ServerResponse> result = userHandler.listenPOSTSignUpUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(ServerResponse.class::isInstance)
                .verifyComplete();

        verify(signUpUseCase, times(1)).signUpUser(userEntity);
    }

    @Test
    @DisplayName("Should return error on SignUp when validation fails")
    void testListenPOSTSignUpUseCaseValidationError() {
        when(validationService.validate(any(SignUpDTO.class)))
                .thenReturn(Mono.error(new RuntimeException("Validation failed")));
        when(serverRequest.bodyToMono(SignUpDTO.class)).thenReturn(Mono.just(signUpDTO));

        StepVerifier.create(userHandler.listenPOSTSignUpUseCase(serverRequest))
                .expectErrorMatches(UnexpectedException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("Should handle POST /login request and return ServerResponse")
    void testListenPOSTSignInUseCase() {
        when(serverRequest.bodyToMono(SignInDTO.class)).thenReturn(Mono.just(signInDTO));

        Mono<ServerResponse> result = userHandler.listenPOSTSignInUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(ServerResponse.class::isInstance)
                .verifyComplete();

        verify(signInUseCase, times(1)).signInUser(signInDTO.email(), signInDTO.password());
    }

    @Test
    @DisplayName("Should return error on SignIn when signInUseCase fails")
    void testListenPOSTSignInUseCaseError() {
        when(signInUseCase.signInUser(anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("SignIn failed")));
        when(serverRequest.bodyToMono(SignInDTO.class)).thenReturn(Mono.just(signInDTO));

        StepVerifier.create(userHandler.listenPOSTSignInUseCase(serverRequest))
                .expectErrorMatches(UnexpectedException.class::isInstance)
                .verify();
    }

    @Test
    @DisplayName("Should handle GET /clientes request and return ServerResponse")
    void testListenGETAllClientsUseCase() {
        UUID userId = UUID.randomUUID();

        var userList = List.of(userEntity);
        when(getUsersByIdUseCase.getUsersByIds(anyList())).thenReturn(Flux.fromIterable(userList));
        when(userMapper.toGetAllClientsResponse(any(User.class)))
                .thenReturn(new GetAllClientsResponse(
                        userEntity.getId(),
                        userEntity.getName(),
                        userEntity.getLastName(),
                        userEntity.getDni(),
                        userEntity.getEmail(),
                        userEntity.getBaseSalary()
                ));

        when(serverRequest.bodyToMono(new ParameterizedTypeReference<List<UUID>>() {}))
                .thenReturn(Mono.just(List.of(userId)));

        Mono<ServerResponse> result = userHandler.listenGETAllClientsUseCase(serverRequest);

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(getUsersByIdUseCase, times(1)).getUsersByIds(List.of(userId));
        verify(userMapper, times(1)).toGetAllClientsResponse(userEntity);
    }
}