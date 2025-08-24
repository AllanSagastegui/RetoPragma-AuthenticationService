package pe.com.ask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.usecase.signup.SignUpUseCase;

@Configuration
@ComponentScan(basePackages = "pe.com.ask.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public SignUpUseCase signUpUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordHasher passwordHasher,
            TransactionalGateway transactionalGateway) {
        return new SignUpUseCase(userRepository, roleRepository, passwordHasher, transactionalGateway);
    }
}