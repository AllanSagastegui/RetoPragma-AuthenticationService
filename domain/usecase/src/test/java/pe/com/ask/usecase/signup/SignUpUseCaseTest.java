package pe.com.ask.usecase.signup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.role.Role;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.RoleNotFoundException;
import pe.com.ask.usecase.exception.UserAlreadyExistsException;
import pe.com.ask.usecase.utils.DEFAULT_ROLE;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {
    @Mock private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock private PasswordHasher passwordHasher;
    @Mock private TransactionalGateway transactionalGateway;
    @Mock private CustomLogger customLogger;

    private SignUpUseCase signUpUseCase;
    private User testUser;
    private Role clientRole;

    @BeforeEach
    void setUp() {
        signUpUseCase = new SignUpUseCase(userRepository, roleRepository, passwordHasher, transactionalGateway, customLogger);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password123")
                .dni("12345678")
                .birthday(LocalDate.of(1990, 1, 1))
                .address("Test Address")
                .phone("987654321")
                .baseSalary(new BigDecimal("2500.50"))
                .build();

        clientRole = Role.builder()
                .id(UUID.randomUUID())
                .name(DEFAULT_ROLE.CLIENT.toString())
                .description("Client")
                .build();
    }

    @Test
    @DisplayName("Should sign up user successfully when email and DNI are unique and role exists")
    void signUpUserSuccess() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByDni(testUser.getDni())).thenReturn(Mono.empty());
        when(roleRepository.findByName(DEFAULT_ROLE.CLIENT.toString())).thenReturn(Mono.just(clientRole));
        when(passwordHasher.hash(testUser.getPassword())).thenReturn("hashedPassword");
        when(userRepository.signUp(any(User.class))).thenReturn(Mono.just(testUser));

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> ((Mono<?>) invocation.getArgument(0)));

        StepVerifier.create(signUpUseCase.signUpUser(testUser))
                .expectNextMatches(user -> user.getEmail().equals("test@example.com") &&
                        user.getPassword().equals("hashedPassword") &&
                        user.getIdRole().equals(clientRole.getId()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void signUpUserEmailExists() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(testUser));
        when(roleRepository.findByName(DEFAULT_ROLE.CLIENT.toString())).thenReturn(Mono.empty());

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> ((Mono<?>) invocation.getArgument(0)));

        StepVerifier.create(signUpUseCase.signUpUser(testUser))
                .expectError(UserAlreadyExistsException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when DNI already exists")
    void signUpUserDniExists() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByDni(testUser.getDni())).thenReturn(Mono.just(testUser));
        when(roleRepository.findByName(DEFAULT_ROLE.CLIENT.toString())).thenReturn(Mono.empty());

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> ((Mono<?>) invocation.getArgument(0)));

        StepVerifier.create(signUpUseCase.signUpUser(testUser))
                .expectError(UserAlreadyExistsException.class)
                .verify();
    }

    @Test
    @DisplayName("Should throw RoleNotFoundException when default role CLIENT is not found")
    void signUpUserRoleNotFound() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByDni(testUser.getDni())).thenReturn(Mono.empty());
        when(roleRepository.findByName("CLIENT")).thenReturn(Mono.empty());

        when(transactionalGateway.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> ((Mono<?>) invocation.getArgument(0)));

        StepVerifier.create(signUpUseCase.signUpUser(testUser))
                .expectError(RoleNotFoundException.class)
                .verify();
    }
}