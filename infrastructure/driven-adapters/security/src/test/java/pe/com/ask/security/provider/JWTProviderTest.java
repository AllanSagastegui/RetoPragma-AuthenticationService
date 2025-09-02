package pe.com.ask.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.com.ask.model.role.Role;
import pe.com.ask.model.role.gateways.RoleRepository;
import pe.com.ask.model.user.User;
import pe.com.ask.security.exception.JwtExpiredException;
import pe.com.ask.security.exception.JwtInvalidSignatureException;
import pe.com.ask.security.exception.JwtMalformedException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.management.relation.RoleNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

class JWTProviderTest {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Mock
    private RoleRepository roleRepository;

    private JWTProvider jwtProvider;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        jwtProvider = new JWTProvider(privateKey, publicKey, roleRepository);
    }

    @Test
    @DisplayName("generateAccessToken → should return Token when role exists")
    void testGenerateAccessTokenSuccess() {
        UUID roleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Role role = new Role();
        role.setId(roleId);
        role.setName("ADMIN");

        User user = new User();
        user.setId(userId);
        user.setDni("12345678");
        user.setIdRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Mono.just(role));

        StepVerifier.create(jwtProvider.generateAccessToken(user))
                .assertNext(token -> {
                    assert token.getToken() != null && !token.getToken().isEmpty();
                })
                .verifyComplete();

        verify(roleRepository).findById(roleId);
    }

    @Test
    @DisplayName("generateAccessToken → should error when role not found")
    void testGenerateAccessTokenRoleNotFound() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setIdRole(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Mono.empty());

        StepVerifier.create(jwtProvider.generateAccessToken(user))
                .expectError(RoleNotFoundException.class)
                .verify();

        verify(roleRepository).findById(roleId);
    }

    @Test
    @DisplayName("isTokenValid → should return true for valid token")
    void testIsTokenValidTrue() {
        String token = Jwts.builder()
                .subject("test")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        StepVerifier.create(jwtProvider.isTokenValid(token))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("isTokenValid → should return false for expired token")
    void testIsTokenValidFalse() {
        String expiredToken = Jwts.builder()
                .subject("test")
                .issuedAt(Date.from(Instant.now().minusSeconds(7200)))
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        StepVerifier.create(jwtProvider.isTokenValid(expiredToken))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("extractAllClaims → map ExpiredJwtException to JwtExpiredException")
    void testExtractAllClaimsExpired() {
        String expiredToken = Jwts.builder()
                .subject("test")
                .issuedAt(Date.from(Instant.now().minusSeconds(7200)))
                .expiration(Date.from(Instant.now().minusSeconds(3600)))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        StepVerifier.create(jwtProvider.extractAllClaims(expiredToken))
                .expectError(JwtExpiredException.class)
                .verify();
    }

    @Test
    @DisplayName("extractAllClaims → map SignatureException to JwtInvalidSignatureException")
    void testExtractAllClaimsInvalidSignature() {
        KeyPair otherKeyPair;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            otherKeyPair = keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String badToken = Jwts.builder()
                .subject("test")
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(otherKeyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();

        StepVerifier.create(jwtProvider.extractAllClaims(badToken))
                .expectError(JwtInvalidSignatureException.class)
                .verify();
    }

    @Test
    @DisplayName("extractAllClaims → map MalformedJwtException to JwtMalformedException")
    void testExtractAllClaimsMalformed() {
        String malformedToken = "this.is.not.a.jwt";

        StepVerifier.create(jwtProvider.extractAllClaims(malformedToken))
                .expectError(JwtMalformedException.class)
                .verify();
    }
}