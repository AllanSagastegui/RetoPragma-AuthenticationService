package pe.com.ask.security.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.token.Token;
import pe.com.ask.model.user.User;
import pe.com.ask.security.provider.JWTProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTTokenRepositoryAdapterTest {

    @Mock
    private JWTProvider jwtProvider;

    private JWTTokenRepositoryAdapter tokenRepositoryAdapter;

    private User testUser;
    private Token testToken;

    @BeforeEach
    void setUp() {
        tokenRepositoryAdapter = new JWTTokenRepositoryAdapter(jwtProvider);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastName("User")
                .email("test@example.com")
                .build();

        testToken = new Token("mockedToken123");
    }

    @Test
    @DisplayName("Should generate access token successfully")
    void generateAccessTokenSuccess() {
        when(jwtProvider.generateAccessToken(testUser)).thenReturn(Mono.just(testToken));

        StepVerifier.create(tokenRepositoryAdapter.generateAccessToken(testUser))
                .expectNextMatches(token -> token.getToken().equals("mockedToken123"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should propagate error when JWTProvider fails")
    void generateAccessTokenError() {
        when(jwtProvider.generateAccessToken(testUser))
                .thenReturn(Mono.error(new RuntimeException("JWT generation failed")));

        StepVerifier.create(tokenRepositoryAdapter.generateAccessToken(testUser))
                .expectErrorMessage("JWT generation failed")
                .verify();
    }
}