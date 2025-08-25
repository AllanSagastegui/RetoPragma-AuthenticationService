package pe.com.ask.security.provider;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.user.User;
import pe.com.ask.security.enums.TokenExpirationTime;
import reactor.core.publisher.Mono;

import javax.management.relation.RoleNotFoundException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final RoleRepository roleRepository;

    public Mono<Token> generateAccessToken(User user) {
        return roleRepository.findById(user.getIdRole())
                .switchIfEmpty(Mono.error(new RoleNotFoundException()))
                .map(role -> Jwts.builder()
                        .subject(user.getId().toString())
                        .issuedAt(new Date())
                        .expiration(Date.from(Instant.now()
                                .plusSeconds(TokenExpirationTime.ACCESS.getSeconds())))
                        .claims(Map.of(
                                "role", role.getName()
                        ))
                        .signWith(privateKey)
                        .compact()
                )
                .map(Token::new);
    }
}