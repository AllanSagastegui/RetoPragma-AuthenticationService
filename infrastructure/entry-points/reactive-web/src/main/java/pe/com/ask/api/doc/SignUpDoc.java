package pe.com.ask.api.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.ask.api.doc.exception.UnauthorizedExceptionDoc;
import pe.com.ask.api.doc.exception.UnexpectedExceptionDoc;
import pe.com.ask.api.doc.exception.UserAlreadyExistsExceptionDoc;
import pe.com.ask.api.doc.exception.ValidationExceptionDoc;
import pe.com.ask.api.dto.request.SignUpDTO;
import pe.com.ask.api.dto.response.SignUpResponse;
import reactor.core.publisher.Mono;

@Component
@Schema(description = "Documentation for user sign-up endpoint")
public class SignUpDoc {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Sign-Up a new user",
            description = "Endpoint for user registration.",
            security = {@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "User successfully registered",
                    content = @Content(
                            schema = @Schema(implementation = SignUpResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Validation error or malformed request",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ValidationExceptionDoc.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: missing or invalid JWT token",
                    content = @Content(
                            schema = @Schema(implementation = UnauthorizedExceptionDoc.class))
            ),
            @ApiResponse(responseCode = "409",
                    description = "Conflict: user already exists",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserAlreadyExistsExceptionDoc.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = UnexpectedExceptionDoc.class)
                    )
            )
    })
    public Mono<SignUpResponse> signUpDoc(
            @RequestBody(description = "Sign Up - Data required to register a new user")
            @org.springframework.web.bind.annotation.RequestBody SignUpDTO dto) {
        return Mono.empty();
    }
}