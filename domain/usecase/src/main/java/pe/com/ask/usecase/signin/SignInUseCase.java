package pe.com.ask.usecase.signin;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
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
    private final CustomLogger customLogger;

    public Mono<Token> signInUser(String email, String password){
        customLogger.trace("Start sign-in User flow");
            return userRepository.findByEmail(email)
                    .doOnSubscribe(sub -> customLogger.trace("Subscribed to findByEmail for {}", email))
                    .doOnNext(user -> customLogger.trace("User found {}", user.getEmail()))
                    .switchIfEmpty(Mono.defer(() -> {
                        customLogger.trace("User not found for {}", email);
                        return Mono.error(new InvalidCredentialsException());
                    }))
                    .flatMap(user -> validatePassword(user, password))
                    .flatMap(tokenRepository::generateAccessToken)
                    .doOnSuccess(token -> customLogger.trace("Token generated successfully for email: {}", email))
                    .doOnError(err -> customLogger.trace("Sign-in error for {}: {}", email, err.getMessage()));

    }

    private Mono<User> validatePassword(User user, String rawPassword){
        customLogger.trace("Validating password for user: {}", user.getEmail());
        return Mono.just(user)
                .filter(u -> passwordHasher.matches(rawPassword, u.getPassword()))
                .doOnNext(u -> customLogger.trace("Password validation passed for user: {}", u.getEmail()))
                .switchIfEmpty(Mono.defer(() -> {
                    customLogger.trace("Password validation failed for user: {}", user.getEmail());
                    return Mono.error(new InvalidCredentialsException());
                }));
    }
}