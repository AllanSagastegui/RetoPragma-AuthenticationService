package pe.com.ask.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "SignUpDTO", description = "Data required for user registration")
public record SignUpDTO(

        @NotBlank(message = "Name cannot be blank")
        @Schema(description = "User's first name", example = "Allan")
        String name,

        @NotBlank(message = "Last name cannot be blank")
        @Schema(description = "User's last name", example = "Sagastegui")
        String lastName,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "A valid email address is required")
        @Schema(description = "User's email", example = "allan.sagastegui@test.com")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Schema(description = "User's password", example = "********")
        String password,

        @Schema(description = "User's birthday", example = "2003-03-11")
        LocalDate birthday,

        @Schema(description = "User's address", example = "Mi Casa, Lima, Peru")
        String address,

        @Schema(description = "User's phone number", example = "987654321")
        String phone,

        @NotNull(message = "Base salary cannot be null")
        @DecimalMin(value= "0.0", inclusive = true, message = "Base salary cannot be less than 0")
        @DecimalMax(value = "15000000.0", inclusive = true, message = "Base salary cannot exceed 15,000,000")
        @Schema(description = "User's base salary", example = "2500.50")
        BigDecimal baseSalary
) { }