package pe.com.ask.r2dbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.ask.model.role.Role;
import pe.com.ask.r2dbc.entity.RoleEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {

    @InjectMocks
    RoleReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    RoleReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private Role domain;
    private RoleEntity entity;

    @BeforeEach
    void setUp() {
        domain = Role.builder()
                .id(UUID.randomUUID())
                .name("TestRole")
                .description("This role is used only for tests")
                .build();

        entity = new RoleEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription()
        );
    }

    @Test
    @DisplayName("Should find role by name successfully")
    void testFindByName() {
        when(repository.findByName("TestRole")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Role.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByName("TestRole"))
                .expectNextMatches(role -> role.getName().equals(domain.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when role name is incorrect")
    void testFindByNameShouldFail() {
        when(repository.findByName("WrongRole"))
                .thenReturn(Mono.error(new RuntimeException("Role not found")));

        StepVerifier.create(repositoryAdapter.findByName("WrongRole"))
                .expectErrorMessage("Role not found")
                .verify();
    }

    @Test
    @DisplayName("Should return empty when role name does not exist")
    void testFindByNameShouldFailWithEmptyValue() {
        when(repository.findByName("UnknownRole")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByName("UnknownRole"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when role name is null")
    void testFindByNameShouldFailWithNull() {
        when(repository.findByName(isNull())).thenReturn(Mono.error(new IllegalArgumentException("Name cannot be null")));

        StepVerifier.create(repositoryAdapter.findByName(null))
                .expectErrorMessage("Name cannot be null")
                .verify();
    }

    @Test
    @DisplayName("Should find role by ID successfully")
    void testFindById() {
        when(repository.findById(domain.getId())).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Role.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findById(domain.getId()))
                .expectNextMatches(role -> role.getId().equals(domain.getId()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when role ID is incorrect")
    void testFindByIdShouldFail() {
        UUID wrongId = UUID.randomUUID();
        when(repository.findById(wrongId))
                .thenReturn(Mono.error(new RuntimeException("Role not found")));

        StepVerifier.create(repositoryAdapter.findById(wrongId))
                .expectErrorMessage("Role not found")
                .verify();
    }

    @Test
    @DisplayName("Should return empty when role ID does not exist")
    void testFindByIdShouldFailWithEmptyValue() {
        UUID unknownId = UUID.randomUUID();
        when(repository.findById(unknownId)).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findById(unknownId))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when role ID is null")
    void testFindByIdShouldFailWithNull() {
        when(repository.findById(isNull(UUID.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("Id cannot be null")));

        StepVerifier.create(repositoryAdapter.findById(null))
                .expectErrorMessage("Id cannot be null")
                .verify();
    }
}
