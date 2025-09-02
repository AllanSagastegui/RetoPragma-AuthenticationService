package pe.com.ask.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pe.com.ask.security.provider.JWTProvider;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter implements WebFilter {

    private final JWTProvider jwtProvider;

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(jwt -> jwtProvider.extractAllClaims(jwt)
                        .flatMap(claims -> {
                            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                            var auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), jwt, authorities);

                            return jwtProvider.isTokenValid(jwt)
                                    .filter(Boolean::booleanValue)
                                    .map(valid -> new SecurityContextImpl(auth));
                        })
                        .onErrorResume(Mono::error)
                )
                .flatMap(context -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context))))
                .switchIfEmpty(chain.filter(exchange));
    }
}