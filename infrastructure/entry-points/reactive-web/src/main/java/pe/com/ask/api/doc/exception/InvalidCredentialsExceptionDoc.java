package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "InvalidCredentialsException",
        description = "Error response returned when user credentials are invalid."
)
public class InvalidCredentialsExceptionDoc {
    @Schema(
            description = "Unique error code for this type of exception.",
            example = "AUTH_INVALID_CREDENTIALS"
    )
    private String errorCode;

    @Schema(
            description = "Short descriptive title of the error.",
            example = "Invalid Credentials"
    )
    private String title;

    @Schema(
            description = "Detailed error message explaining the reason for the failure.",
            example = "The credentials you provided are incorrect. Please try again."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with this error.",
            example = "401"
    )
    private int status;

    @Schema(
            description = "The timestamp when the error occurred.",
            example = "2025-08-28T16:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Additional information related to the error (if applicable).",
            example = "{\"credentials\": \"Invalid email or password\"}"
    )
    private Object errors;
}