package pe.com.ask.security.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.token.gateways.TokenRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.security.provider.JWTProvider;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JWTTokenRepositoryAdapter implements TokenRepository {

    private final JWTProvider jwtProvider;

    @Override
    public Mono<Token> generateAccessToken(User user) {
        return jwtProvider.generateAccessToken(user);
    }
}