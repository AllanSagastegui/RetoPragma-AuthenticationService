package pe.com.ask.usecase.signin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.InvalidCredentialsException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignInUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private PasswordHasher passwordHasher;
    @Mock private CustomLogger customLogger;

    private SignInUseCase signInUseCase;

    private final String email = "test@example.com";
    private final String password = "password123";
    private User testUser;
    private Token testToken;

    @BeforeEach
    void setUp() {
        signInUseCase = new SignInUseCase(userRepository, tokenRepository, passwordHasher, customLogger);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastName("User")
                .email(email)
                .password("hashedPassword")
                .dni("12345678")
                .birthday(LocalDate.of(1990, 1, 1))
                .address("Test Address")
                .phone("987654321")
                .baseSalary(new BigDecimal("2500.50"))
                .build();

        testToken = new Token("token123");
    }

    @Test
    @DisplayName("Should sign in successfully when user exists and password is valid")
    void signInUserSuccess() {
        when(userRepository.findByEmail(email)).thenReturn(Mono.just(testUser));
        when(passwordHasher.matches(password, testUser.getPassword())).thenReturn(true);
        when(tokenRepository.generateAccessToken(testUser)).thenReturn(Mono.just(testToken));

        StepVerifier.create(signInUseCase.signInUser(email, password))
                .expectNext(testToken)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when user is not found")
    void signInUserUserNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(signInUseCase.signInUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is invalid")
    void signInUserInvalidPassword() {
        when(userRepository.findByEmail(email)).thenReturn(Mono.just(testUser));
        when(passwordHasher.matches(password, testUser.getPassword())).thenReturn(false);

        StepVerifier.create(signInUseCase.signInUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
}