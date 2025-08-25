package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "SignUpResponse", description = "Response after user registration")
public record SignUpResponse(

        @Schema(description = "User's Name", example = "Allan")
        String name,

        @Schema(description = "User's LastName", example = "Sagastegui")
        String lastName,

        @Schema(description = "National ID (DNI) of the applicant", example = "12345678")
        String dni,

        @Schema(description = "User's Email", example = "allan.sagastegui@test.com")
        String email,

        @Schema(description = "User's birthday", example = "2003-03-11")
        LocalDate birthday,

        @Schema(description = "User's address", example = "Mi Casa, Lima, Peru")
        String address,

        @Schema(description = "User's phone number", example = "987654321")
        String phone,

        @Schema(description = "User's base salary", example = "2500.50")
        BigDecimal baseSalary
) {}