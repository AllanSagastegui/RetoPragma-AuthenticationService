package pe.com.ask.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.user.User;
import pe.com.ask.security.enums.TokenExpirationTime;
import pe.com.ask.security.exception.JwtExpiredException;
import pe.com.ask.security.exception.JwtInvalidSignatureException;
import pe.com.ask.security.exception.JwtMalformedException;
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
                                "role", role.getName(),
                                "userId",  user.getId().toString(),
                                "userDni", user.getDni()
                        ))
                        .signWith(privateKey, Jwts.SIG.RS256)
                        .compact()
                )
                .map(Token::new);
    }

    public Mono<Claims> extractAllClaims(String token) {
        return Mono.fromCallable(() -> Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
        )
                .onErrorMap(io.jsonwebtoken.ExpiredJwtException.class, ex -> new JwtExpiredException())
                .onErrorMap(io.jsonwebtoken.security.SignatureException.class, ex -> new JwtInvalidSignatureException())
                .onErrorMap(io.jsonwebtoken.MalformedJwtException.class, ex -> new JwtMalformedException());
    }

    public Mono<Boolean> isTokenValid(String token) {
        return extractAllClaims(token)
                .map(claims -> claims.getExpiration().after(new Date()))
                .onErrorReturn(false);
    }
}