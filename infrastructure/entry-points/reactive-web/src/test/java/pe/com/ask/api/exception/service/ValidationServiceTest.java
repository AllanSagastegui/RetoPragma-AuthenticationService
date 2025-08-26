package pe.com.ask.api.exception.service;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.com.ask.api.exception.model.ValidationException;
import reactor.test.StepVerifier;

import jakarta.validation.constraints.NotNull;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            validationService = new ValidationService(validator);
        }
    }

    static class TestDTO {
        @NotNull
        String name;
        TestDTO(String name) { this.name = name; }
    }

    @Test
    void validateValidObject() {
        TestDTO dto = new TestDTO("Allan");

        StepVerifier.create(validationService.validate(dto))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void validateInvalidObject() {
        TestDTO dto = new TestDTO(null);

        StepVerifier.create(validationService.validate(dto))
                .expectError(ValidationException.class)
                .verify();
    }
}