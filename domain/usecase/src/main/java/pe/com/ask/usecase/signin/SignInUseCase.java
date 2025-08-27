package pe.com.ask.usecase.signin;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.InvalidCredentialsException;
import pe.com.ask.usecase.utils.logmessages.SignInUseCaseLog;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignInUseCase {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordHasher passwordHasher;
    private final CustomLogger customLogger;

    public Mono<Token> signInUser(String email, String password){
        customLogger.trace(SignInUseCaseLog.START_FLOW);
        return userRepository.findByEmail(email)
                .doOnSubscribe(sub -> customLogger.trace(SignInUseCaseLog.SUBSCRIBED_FIND_BY_EMAIL, email))
                .doOnNext(user -> customLogger.trace(SignInUseCaseLog.USER_FOUND, user.getEmail()))
                .switchIfEmpty(Mono.defer(() -> {
                    customLogger.trace(SignInUseCaseLog.USER_NOT_FOUND, email);
                    return Mono.error(new InvalidCredentialsException());
                }))
                .flatMap(user -> validatePassword(user, password))
                .flatMap(tokenRepository::generateAccessToken)
                .doOnSuccess(token -> customLogger.trace(SignInUseCaseLog.TOKEN_GENERATED, email))
                .doOnError(err -> customLogger.trace(SignInUseCaseLog.SIGNIN_ERROR, email, err.getMessage()));
    }

    private Mono<User> validatePassword(User user, String rawPassword){
        customLogger.trace(SignInUseCaseLog.VALIDATING_PASSWORD, user.getEmail());
        return Mono.just(user)
                .filter(u -> passwordHasher.matches(rawPassword, u.getPassword()))
                .doOnNext(u -> customLogger.trace(SignInUseCaseLog.PASSWORD_VALIDATION_PASSED, u.getEmail()))
                .switchIfEmpty(Mono.defer(() -> {
                    customLogger.trace(SignInUseCaseLog.PASSWORD_VALIDATION_FAILED, user.getEmail());
                    return Mono.error(new InvalidCredentialsException());
                }));
    }
}