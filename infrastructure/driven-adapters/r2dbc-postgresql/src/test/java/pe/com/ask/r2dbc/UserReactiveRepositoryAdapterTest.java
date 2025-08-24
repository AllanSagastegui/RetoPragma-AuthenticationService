package pe.com.ask.r2dbc;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    // TODO: change four you own tests
    /*
    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UserEntity entity = new UserEntity();
        entity.setId("1");
        entity.setName("Test");

        when(repository.findById("1")).thenReturn(Mono.just(entity));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<User> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals("1") && value.getName().equals("Test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity entity = new UserEntity();
        entity.setId("1");
        entity.setName("Test");

        User user = User.builder().id("1").name("Test").build();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map("test", Object.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        UserEntity entity = new UserEntity();
        entity.setId("1");
        entity.setName("Test");

        User user = User.builder().id("1").name("Test").build();

        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        when(mapper.map("test", Object.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findByExample(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        UserEntity entity = new UserEntity();
        entity.setId("1");
        entity.setName("Test");

        User user = User.builder().id("1").name("Test").build();

        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

     */
}
