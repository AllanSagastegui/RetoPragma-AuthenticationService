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
import pe.com.ask.api.doc.exception.InvalidCredentialsExceptionDoc;
import pe.com.ask.api.doc.exception.UnexpectedExceptionDoc;
import pe.com.ask.api.doc.exception.ValidationExceptionDoc;
import pe.com.ask.api.dto.request.SignInDTO;
import pe.com.ask.api.dto.response.SignInResponse;
import pe.com.ask.api.dto.response.SignUpResponse;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "SignInDoc",
        description = "API documentation for user authentication (Sign-In). " +
                "This endpoint allows users to log in with their registered email and password " +
                "to obtain a valid JWT token for accessing secured endpoints."
)
public class SignInDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Sign-In user",
            description = "Authenticates a user using their email and password. " +
                    "If the credentials are valid, a JWT token is returned along with user details. " +
                    "This token must be included in the `Authorization` header for subsequent secured API calls."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Login successful.",
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
                    description = "Unauthorized: Invalid credentials (wrong email or password).",
                    content = @Content(
                            schema = @Schema(implementation = InvalidCredentialsExceptionDoc.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = UnexpectedExceptionDoc.class)
                    )
            )
    })
    public Mono<SignInResponse> signInDoc(
            @RequestBody(description = "Sign-In credentials including the user's email and password. " +
                    "Both fields are mandatory and must match a registered user."
            )
            @org.springframework.web.bind.annotation.RequestBody SignInDTO dto) {
        return Mono.empty();
    }
}