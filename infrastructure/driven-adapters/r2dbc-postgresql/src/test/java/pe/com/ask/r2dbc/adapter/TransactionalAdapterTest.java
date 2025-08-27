package pe.com.ask.r2dbc.adapter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TransactionalAdapterTest {

    @Mock private TransactionalOperator transactionalOperator;

    private TransactionalAdapter transactionalAdapter;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        transactionalAdapter = new TransactionalAdapter(transactionalOperator);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should delegate Mono.just execution to TransactionalOperator")
    void testExecuteInTransactionDelegatesToTransactionalOperator() {
        String expectedValue = "Hello World";
        Mono<String> action = Mono.just(expectedValue);

        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.executeInTransaction(action))
                .expectNext(expectedValue)
                .verifyComplete();

        verify(transactionalOperator, times(1)).transactional(action);
    }

    @Test
    @DisplayName("Should delegate Mono.empty execution to TransactionalOperator")
    void testExecuteInTransactionWithEmptyMono() {
        Mono<String> action = Mono.empty();

        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.executeInTransaction(action))
                .verifyComplete();

        verify(transactionalOperator, times(1)).transactional(action);
    }

    @Test
    @DisplayName("Should delegate Mono.error execution to TransactionalOperator")
    void testExecuteInTransactionWithError() {
        RuntimeException exception = new RuntimeException("Test Error");
        Mono<String> action = Mono.error(exception);

        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.executeInTransaction(action))
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Test Error"))
                .verify();

        verify(transactionalOperator, times(1)).transactional(action);
    }
}