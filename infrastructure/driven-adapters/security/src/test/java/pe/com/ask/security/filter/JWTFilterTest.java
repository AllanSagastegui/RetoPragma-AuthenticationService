package pe.com.ask.security.filter;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import pe.com.ask.security.provider.JWTProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class JWTFilterTest {

    @Mock
    private JWTProvider jwtProvider;

    @Mock
    private WebFilterChain filterChain;

    private JWTFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtFilter = new JWTFilter(jwtProvider);
    }

    @Test
    @DisplayName("No Authorization header → continue chain")
    void testNoAuthHeader() {
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test").build());
        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, filterChain))
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
        verifyNoMoreInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Authorization header does not start with Bearer → continue chain")
    void testInvalidAuthHeader() {
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/test")
                        .header(HttpHeaders.AUTHORIZATION, "Basic abc123")
                        .build()
        );
        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, filterChain))
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
        verifyNoMoreInteractions(jwtProvider);
    }

    @Test
    @DisplayName("JWT invalid → switchIfEmpty continues chain")
    void testInvalidJWT() {
        String jwt = "valid.jwt";
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .build()
        );

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("user@example.com");
        when(claims.get("role")).thenReturn("USER");

        when(jwtProvider.extractAllClaims(jwt)).thenReturn(Mono.just(claims));
        when(jwtProvider.isTokenValid(jwt)).thenReturn(Mono.just(false));
        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtFilter.filter(exchange, filterChain))
                .verifyComplete();

        verify(jwtProvider).extractAllClaims(jwt);
        verify(jwtProvider).isTokenValid(jwt);
        verify(filterChain).filter(exchange);
    }
}