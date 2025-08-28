package pe.com.ask.api.doc.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "UserAlreadyExistsException",
        description = "Represents an error returned when trying to register a user that already exists in the system."
)
public class UserAlreadyExistsExceptionDoc {
    @Schema(
            description = "Unique error code for this type of exception.",
            example = "AUTH_USER_ALREADY_EXISTS"
    )
    private String errorCode;

    @Schema(
            description = "Short descriptive title of the error.",
            example = "User Already Exists"
    )
    private String title;

    @Schema(
            description = "Detailed error message explaining the reason for the failure.",
            example = "The email address is already registered in the system."
    )
    private String message;

    @Schema(
            description = "HTTP status code associated with this error.",
            example = "409"
    )
    private int status;

    @Schema(
            description = "The timestamp when the error occurred.",
            example = "2025-08-28T15:23:45"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Additional information related to the error (if applicable).",
            example = "Not Applicable"
    )
    private Object errors;
}