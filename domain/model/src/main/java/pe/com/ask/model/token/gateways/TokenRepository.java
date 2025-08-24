package pe.com.ask.model.token.gateways;

import pe.com.ask.model.token.Token;
import pe.com.ask.model.user.User;
import reactor.core.publisher.Mono;

public interface TokenRepository {
    Mono<Token> generateAccessToken(User user);
}