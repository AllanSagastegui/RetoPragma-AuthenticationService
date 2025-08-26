package pe.com.ask.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.user.User;
import pe.com.ask.r2dbc.entity.UserEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private User domain;
    private UserEntity entity;

    @BeforeEach
    void setup() {
        domain = User.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .lastName("User")
                .dni("12345678")
                .email("test@example.com")
                .password("password123")
                .birthday(LocalDate.of(1990, 1, 1))
                .address("123 Test St")
                .phone("999999999")
                .baseSalary(new BigDecimal("5000"))
                .idRole(UUID.randomUUID())
                .build();

        entity = new UserEntity(
                domain.getId(),
                domain.getName(),
                domain.getLastName(),
                domain.getDni(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getBirthday(),
                domain.getAddress(),
                domain.getPhone(),
                domain.getBaseSalary(),
                domain.getIdRole()
        );
    }

    @Test
    void testSignUp() {
        when(mapper.map(domain, UserEntity.class)).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(domain);
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        StepVerifier.create(repositoryAdapter.signUp(domain))
                .expectNextMatches(result ->
                        result.getId().equals(domain.getId()) &&
                                result.getEmail().equals(domain.getEmail()))
                .verifyComplete();
    }

    @Test
    void testSignUpShouldFail() {
        when(mapper.map(domain, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("Error registering user")));

        StepVerifier.create(repositoryAdapter.signUp(domain))
                .expectErrorMessage("Error registering user")
                .verify();
    }

    @Test
    void testSignUpShouldFailWithEmptyValue() {
        when(mapper.map(domain, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("Entity is empty")));

        StepVerifier.create(repositoryAdapter.signUp(domain))
                .expectErrorMessage("Entity is empty")
                .verify();
    }

    @Test
    void testSignUpShouldFailWithNull() {
        when(mapper.map(domain, UserEntity.class)).thenReturn(null);
        when(repository.save(null)).thenReturn(Mono.error(new RuntimeException("Entity is null")));

        StepVerifier.create(repositoryAdapter.signUp(domain))
                .expectErrorMessage("Entity is null")
                .verify();
    }

    @Test
    void testFindByEmail() {
        when(repository.findByEmail("test@example.com")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByEmail("test@example.com"))
                .expectNextMatches(user -> user.getEmail().equals(domain.getEmail()))
                .verifyComplete();
    }

    @Test
    void testFindByEmailShouldFail() {
        when(repository.findByEmail("wrong@example.com")).thenReturn(Mono.error(new RuntimeException("User not found")));

        StepVerifier.create(repositoryAdapter.findByEmail("wrong@example.com"))
                .expectErrorMessage("User not found")
                .verify();
    }

    @Test
    void testFindByEmailShouldFailWithEmptyValue() {
        when(repository.findByEmail("empty@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByEmail("empty@example.com"))
                .expectComplete()
                .verify();
    }

    @Test
    void testFindByEmailShouldFailWithNull() {
        when(repository.findByEmail(isNull())).thenReturn(Mono.error(new RuntimeException("Email is null")));

        StepVerifier.create(repositoryAdapter.findByEmail(null))
                .expectErrorMessage("Email is null")
                .verify();
    }

    @Test
    void testFindByDni() {
        when(repository.findByDni("12345678")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByDni("12345678"))
                .expectNextMatches(user -> user.getDni().equals(domain.getDni()))
                .verifyComplete();
    }

    @Test
    void testFindByDniShouldFail() {
        when(repository.findByDni("87654321")).thenReturn(Mono.error(new RuntimeException("User not found")));

        StepVerifier.create(repositoryAdapter.findByDni("87654321"))
                .expectErrorMessage("User not found")
                .verify();
    }

    @Test
    void testFindByDniShouldFailWithEmptyValue() {
        when(repository.findByDni("")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByDni(""))
                .expectComplete()
                .verify();
    }

    @Test
    void testFindByDniShouldFailWithNull() {
        when(repository.findByDni(isNull())).thenReturn(Mono.error(new RuntimeException("DNI is null")));

        StepVerifier.create(repositoryAdapter.findByDni(null))
                .expectErrorMessage("DNI is null")
                .verify();
    }
}