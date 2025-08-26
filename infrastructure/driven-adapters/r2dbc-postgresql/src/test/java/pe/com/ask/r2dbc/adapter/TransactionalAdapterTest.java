package pe.com.ask.r2dbc.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TransactionalAdapterTest {

    @Mock
    private TransactionalOperator transactionalOperator;

    private TransactionalAdapter transactionalAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionalAdapter = new TransactionalAdapter(transactionalOperator);
    }

    @Test
    void testExecuteInTransactionDelegatesToTransactionalOperator() {
        String expectedValue = "Hello Transaction";
        Mono<String> action = Mono.just(expectedValue);

        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.executeInTransaction(action))
                .expectNext(expectedValue)
                .verifyComplete();

        verify(transactionalOperator, times(1)).transactional(action);
    }

    @Test
    void testExecuteInTransactionWithEmptyMono() {
        Mono<String> action = Mono.empty();

        when(transactionalOperator.transactional(action)).thenReturn(action);

        StepVerifier.create(transactionalAdapter.executeInTransaction(action))
                .verifyComplete();

        verify(transactionalOperator, times(1)).transactional(action);
    }

    @Test
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