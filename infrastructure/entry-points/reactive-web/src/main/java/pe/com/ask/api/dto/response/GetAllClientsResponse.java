package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "GetAllClientsResponse", description = "Response after user registration")
public record GetAllClientsResponse(
        @Schema(description = "User's id", example = "jsdjad-dasd...")
        UUID id,

        @Schema(description = "User's Name", example = "Allan")
        String name,

        @Schema(description = "User's LastName", example = "Sagastegui")
        String lastName,

        @Schema(description = "National ID (DNI) of the applicant", example = "12345678")
        String dni,

        @Schema(description = "User's Email", example = "allan.sagastegui@test.com")
        String email,

        @Schema(description = "User's base salary", example = "2500.50")
        BigDecimal baseSalary
) { }