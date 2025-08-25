package pe.com.ask.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import pe.com.ask.model.user.User;
import pe.com.ask.r2dbc.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

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

        System.out.println("=== Starting testSignUp ===");
        System.out.println("Domain user before save: " + domain);

        StepVerifier.create(
                        repositoryAdapter.signUp(domain)
                                .doOnNext(result -> System.out.println("Result from signUp: " + result))
                )
                .expectNextMatches(result ->
                        result.getId().equals(domain.getId()) &&
                                result.getEmail().equals(domain.getEmail()) &&
                                result.getName().equals(domain.getName()) &&
                                result.getLastName().equals(domain.getLastName()) &&
                                result.getPassword().equals(domain.getPassword()) &&
                                result.getBirthday().equals(domain.getBirthday()) &&
                                result.getAddress().equals(domain.getAddress()) &&
                                result.getPhone().equals(domain.getPhone()) &&
                                result.getBaseSalary().equals(domain.getBaseSalary()) &&
                                result.getIdRole().equals(domain.getIdRole())
                )
                .verifyComplete();

        System.out.println("=== Finished testSignUp ===");
    }

    @Test
    void testExistsByEmail() {
        when(repository.existsByEmail("test@example.com")).thenReturn(Mono.just(true));

        StepVerifier.create(repositoryAdapter.existsByEmail("test@example.com")
                        .doOnNext(exists -> System.out.println("ExistsByEmail: " + exists)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testFindByEmail() {
        when(repository.findByEmail("test@example.com")).thenReturn(Mono.just(domain));

        StepVerifier.create(repositoryAdapter.findByEmail("test@example.com")
                        .doOnNext(result -> System.out.println("FindByEmail result: " + result)))
                .expectNextMatches(user -> user.getEmail().equals(domain.getEmail()))
                .verifyComplete();
    }
}
