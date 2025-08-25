package pe.com.ask.usecase.signin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.ask.model.gateways.UseCaseLogger;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.InvalidCredentialsException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignInUseCase {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordHasher passwordHasher;
    private final UseCaseLogger useCaseLogger;

    public Mono<Token> signInUser(String email, String password){
        useCaseLogger.trace("Start sign-in User flow");
            return userRepository.findByEmail(email)
                    .doOnSubscribe(sub -> useCaseLogger.trace("Subscribed to findByEmail for {}", email))
                    .doOnNext(user -> useCaseLogger.trace("User found {}", user.getEmail()))
                    .switchIfEmpty(Mono.defer(() -> {
                        useCaseLogger.trace("User not found for {}", email);
                        return Mono.error(new InvalidCredentialsException());
                    }))
                    .flatMap(user -> validatePassword(user, password))
                    .flatMap(tokenRepository::generateAccessToken)
                    .doOnSuccess(token -> useCaseLogger.trace("Token generated successfully for email: {}", email))
                    .doOnError(err -> useCaseLogger.trace("Sign-in error for {}: {}", email, err.getMessage()));

    }

    private Mono<User> validatePassword(User user, String rawPassword){
        useCaseLogger.trace("Validating password for user: {}", user.getEmail());
        return Mono.just(user)
                .filter(u -> passwordHasher.matches(rawPassword, u.getPassword()))
                .doOnNext(u -> useCaseLogger.trace("Password validation passed for user: {}", u.getEmail()))
                .switchIfEmpty(Mono.defer(() -> {
                    useCaseLogger.trace("Password validation failed for user: {}", user.getEmail());
                    return Mono.error(new InvalidCredentialsException());
                }));
    }
}