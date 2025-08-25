package pe.com.ask.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SignInResponse", description = "Response after user authentication")
public record SignInResponse(
        @Schema(description = "JWT token", example = "eyJhbG...")
        String token
) { }