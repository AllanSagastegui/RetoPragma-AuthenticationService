package pe.com.ask.usecase.signin;

import lombok.RequiredArgsConstructor;
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

    public Mono<Token> signInUser(String email, String password){
            return userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new InvalidCredentialsException()))
                    .flatMap(user -> validatePassword(user, password))
                    .flatMap(tokenRepository::generateAccessToken);
    }

    private Mono<User> validatePassword(User user, String rawPassword){
        return passwordHasher.matches(rawPassword, user.getPassword())
                ? Mono.just(user)
                : Mono.error(new InvalidCredentialsException());
    }
}