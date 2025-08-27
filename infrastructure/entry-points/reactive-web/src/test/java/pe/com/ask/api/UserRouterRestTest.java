package pe.com.ask.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import pe.com.ask.api.exception.GlobalExceptionFilter;
import pe.com.ask.api.exception.model.ValidationException;
import pe.com.ask.api.exception.service.ValidationService;
import pe.com.ask.api.mapper.TokenMapper;
import pe.com.ask.api.mapper.UserMapper;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.user.User;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ContextConfiguration(classes = {
        UserRouterRest.class,
        UserHandler.class,
        GlobalExceptionFilter.class
})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean private SignUpUseCase signUpUseCase;
    @MockitoBean private SignInUseCase signInUseCase;
    @MockitoBean private UserMapper userMapper;
    @MockitoBean private TokenMapper tokenMapper;
    @MockitoBean private ValidationService validationService;
    @MockitoBean private CustomLogger customLogger;

    private SignUpDTO signUpDTO;
    private SignInDTO signInDTO;

    private Token tokenEntity;

    @BeforeEach
    void setUp() {
        User userEntity = new User();
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

        Mockito.when(validationService.validate(any(SignUpDTO.class)))
                .thenReturn(Mono.just(signUpDTO));
        Mockito.when(validationService.validate(any(SignInDTO.class)))
                .thenReturn(Mono.just(signInDTO));

        Mockito.when(userMapper.toEntity(any(SignUpDTO.class)))
                .thenReturn(userEntity);
        Mockito.when(userMapper.toResponse(any(User.class)))
                .thenReturn(signUpResponse);

        Mockito.when(signUpUseCase.signUpUser(any(User.class)))
                .thenReturn(Mono.just(userEntity));

        Mockito.when(signInUseCase.signInUser(anyString(), anyString()))
                .thenReturn(Mono.just(tokenEntity));
        Mockito.when(tokenMapper.toResponse(any(Token.class)))
                .thenReturn(signInResponse);

        UserHandler handler = new UserHandler(userMapper, tokenMapper, validationService, customLogger, signUpUseCase, signInUseCase);
        webTestClient = WebTestClient.bindToRouterFunction(
                new UserRouterRest().userRouterFunction(handler, new GlobalExceptionFilter(customLogger))
        ).build();
    }

    @Test
    @DisplayName("Should return 201 Created when sign-up request is successful")
    void testSignUpEndpointSuccess() {
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SignUpResponse.class)
                .value(response -> {
                    Assertions.assertThat(response.email()).isEqualTo(signUpDTO.email());
                    Assertions.assertThat(response.name()).isEqualTo(signUpDTO.name());
                });
    }

    @Test
    @DisplayName("Should return 200 OK when sign-in request is successful")
    void testSignInEndpointSuccess() {
        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signInDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SignInResponse.class)
                .value(response -> Assertions.assertThat(response.token()).isEqualTo(tokenEntity.getToken()));
    }

    @Test
    @DisplayName("Should return validation error when sign-up request fails validation")
    void testSignUpEndpointValidationFailure() {
        ValidationException validationException = new ValidationException(Map.of(
                "email", "Email is required"
        ));

        Mockito.when(validationService.validate(any(SignUpDTO.class)))
                .thenReturn(Mono.error(validationException));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDTO)
                .exchange()
                .expectStatus().isEqualTo(validationException.getStatus())
                .expectBody()
                .jsonPath("$.errors.email").isEqualTo("Email is required");
    }

    @Test
    @DisplayName("Should return validation error when sign-in request fails validation")
    void testSignInEndpointValidationFailure() {
        ValidationException validationException = new ValidationException(Map.of(
                "email", "Email is required"
        ));

        Mockito.when(validationService.validate(any(SignInDTO.class)))
                .thenReturn(Mono.error(validationException));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signInDTO)
                .exchange()
                .expectStatus().isEqualTo(validationException.getStatus())
                .expectBody()
                .jsonPath("$.errors.email").isEqualTo("Email is required");
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error when unexpected exception occurs during sign-up")
    void testSignUpUnexpectedException() {
        Mockito.when(validationService.validate(any(SignUpDTO.class)))
                .thenReturn(Mono.error(new RuntimeException()));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo("500");
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error when unexpected exception occurs during sign-in")
    void testSignInUnexpectedException() {
        Mockito.when(validationService.validate(any(SignInDTO.class)))
                .thenReturn(Mono.error(new RuntimeException()));

        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signInDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo("500");
    }
}