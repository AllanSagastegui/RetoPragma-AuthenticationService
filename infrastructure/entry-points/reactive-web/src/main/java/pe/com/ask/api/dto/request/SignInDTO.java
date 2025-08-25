package pe.com.ask.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "SignInDTO", description = "Data required for user login")
public record SignInDTO(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "A valid email address is required")
        @Schema(description = "User's email", example = "allan.sagastegui@test.com")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Schema(description = "User's password", example = "********")
        String password
) { }