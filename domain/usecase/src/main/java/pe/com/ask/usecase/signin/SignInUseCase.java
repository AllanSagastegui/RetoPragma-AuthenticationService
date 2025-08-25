package pe.com.ask.usecase.signin;

import lombok.RequiredArgsConstructor;
import pe.com.ask.model.gateways.TransactionalGateway;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.PasswordHasher;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.exception.InvalidCredentialsException;
import pe.com.ask.usecase.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignInUseCase {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordHasher passwordHasher;
    private final TransactionalGateway transactionalGateway;

    public Mono<Token> signInUser(String email, String password){
        return transactionalGateway.executeInTransaction(
            userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new UserNotFoundException()))
                    .flatMap(user -> validatePassword(user, password))
                    .flatMap(tokenRepository::generateAccessToken)
        );
    }

    private Mono<User> validatePassword(User user, String rawPassword){
        return passwordHasher.matches(rawPassword, user.getPassword())
                ? Mono.just(user)
                : Mono.error(new InvalidCredentialsException());
    }
}