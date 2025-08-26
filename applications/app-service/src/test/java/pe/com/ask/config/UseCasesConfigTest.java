package pe.com.ask.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.signin.SignInUseCase;
import pe.com.ask.usecase.signup.SignUpUseCase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class UseCasesConfigTest {

    @Test
    void testSignUpUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {

            SignUpUseCase signUpUseCase = context.getBean(SignUpUseCase.class);
            assertNotNull(signUpUseCase, "SignUpUseCase bean should be registered");
        }
    }

    @Test
    void testSignInUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {

            SignInUseCase signInUseCase = context.getBean(SignInUseCase.class);
            assertNotNull(signInUseCase, "SignInUseCase bean should be registered");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean UserRepository userRepository() { return mock(UserRepository.class); }
        @Bean RoleRepository roleRepository() { return mock(RoleRepository.class); }
        @Bean TokenRepository tokenRepository() { return mock(TokenRepository.class); }
        @Bean PasswordHasher passwordHasher() { return mock(PasswordHasher.class); }
        @Bean TransactionalGateway transactionalGateway() { return mock(TransactionalGateway.class); }
        @Bean CustomLogger customLogger() { return mock(CustomLogger.class); }
    }
}