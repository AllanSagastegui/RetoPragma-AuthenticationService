package pe.com.ask.usecase.getusersbyid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.ask.model.gateways.CustomLogger;
import pe.com.ask.model.user.User;
import pe.com.ask.model.user.gateways.UserRepository;
import pe.com.ask.usecase.utils.logmessages.GetAllClientsLog;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersByIdUseCaseTest {

    @InjectMocks
    private GetUsersByIdUseCase getUsersByIdUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomLogger logger;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testUser = User.builder()
                .id(testUserId)
                .name("Allan")
                .lastName("Sagastegui")
                .email("allan@example.com")
                .dni("12345678")
                .password("password123")
                .birthday(LocalDate.of(1990, 1, 1))
                .address("Lima, Peru")
                .phone("987654321")
                .baseSalary(new BigDecimal("2500"))
                .build();
    }

    @Test
    @DisplayName("Should return users by IDs successfully")
    void getUsersByIdsSuccess() {
        List<UUID> ids = List.of(testUserId);

        when(userRepository.getUsersByIds(ids)).thenReturn(Flux.just(testUser));

        StepVerifier.create(getUsersByIdUseCase.getUsersByIds(ids))
                .expectNextMatches(user -> user.getId().equals(testUserId))
                .verifyComplete();

        verify(logger).trace(GetAllClientsLog.START_FLOW);
        verify(logger).trace(GetAllClientsLog.CLIENT_FOUND, testUser.getEmail());
        verify(logger).trace(GetAllClientsLog.END_FLOW);
        verifyNoMoreInteractions(logger);
    }

    @Test
    @DisplayName("Should return empty when no users found")
    void getUsersByIdsEmpty() {
        List<UUID> ids = List.of(UUID.randomUUID());

        when(userRepository.getUsersByIds(ids)).thenReturn(Flux.empty());

        StepVerifier.create(getUsersByIdUseCase.getUsersByIds(ids))
                .expectComplete()
                .verify();

        verify(logger).trace(GetAllClientsLog.START_FLOW);
        verify(logger).trace(GetAllClientsLog.END_FLOW);
        verifyNoMoreInteractions(logger);
    }

    @Test
    @DisplayName("Should handle repository error")
    void getUsersByIdsError() {
        List<UUID> ids = List.of(testUserId);

        when(userRepository.getUsersByIds(ids))
                .thenReturn(Flux.error(new RuntimeException("Database error")));

        StepVerifier.create(getUsersByIdUseCase.getUsersByIds(ids))
                .expectErrorMessage("Database error")
                .verify();

        verify(logger).trace(GetAllClientsLog.START_FLOW);
        verify(logger).trace(GetAllClientsLog.ERROR, "Database error");
        verifyNoMoreInteractions(logger);
    }
}