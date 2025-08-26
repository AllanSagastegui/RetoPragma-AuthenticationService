package pe.com.ask.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(name = "SignUpDTO", description = "Data required for user registration")
public record SignUpDTO(

        @NotBlank(message = "Name cannot be blank")
        @Schema(description = "User's first name", example = "Allan")
        String name,

        @NotBlank(message = "Last name cannot be blank")
        @Schema(description = "User's last name", example = "Sagastegui")
        String lastName,

        @NotBlank(message = "DNI cannot be blank")
        @Pattern(regexp = "\\d{8}", message = "DNI must contain exactly 8 digits")
        @Schema(description = "National ID (DNI) of the applicant", example = "12345678")
        String dni,

        @NotBlank(message = "Email cannot be blank")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "A valid email address is required (e.g. allan.sagastegui@test.com)"
        )
        @Schema(description = "User's email", example = "allan.sagastegui@test.com")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Schema(description = "User's password", example = "********")
        String password,

        @NotNull(message = "Birthday is required")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Birthday must be in format yyyy-MM-dd"
        )
        @Schema(description = "User's birthday", example = "2003-03-11")
        String birthday,

        @NotBlank(message = "Address cannot be blank")
        @Schema(description = "User's address", example = "Mi Casa, Lima, Peru")
        String address,

        @NotBlank(message = "Phone cannot be blank")
        @Schema(description = "User's phone number", example = "987654321")
        String phone,

        @NotNull(message = "Base salary cannot be null")
        @DecimalMin(value= "0.0", inclusive = true, message = "Base salary cannot be less than 0")
        @DecimalMax(value = "15000000.0", inclusive = true, message = "Base salary cannot exceed 15,000,000")
        @Schema(description = "User's base salary", example = "2500.50")
        BigDecimal baseSalary
) { }