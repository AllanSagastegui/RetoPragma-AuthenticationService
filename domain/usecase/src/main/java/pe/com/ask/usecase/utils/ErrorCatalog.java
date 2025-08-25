package pe.com.ask.usecase.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum ErrorCatalog {

    INVALID_CREDENTIALS(
            "AUTH_INVALID_CREDENTIALS",
            "Invalid Credentials",
            "The credentials you provided are incorrect. Please try again.",
            401,
            Map.of("credentials", "Invalid email or password")
    ),
    ROLE_NOT_FOUND(
            "AUTH_ROLE_NOT_FOUND",
            "Role Not Found",
            "The role you specified does not exist. Please check and try again.",
            404,
            Map.of("role", "The specified role does not exist")
    ),
    USER_ALREADY_EXISTS(
            "AUTH_USER_ALREADY_EXISTS",
            "User Already Exists",
            "A user with this email already exists. Please use a different email.",
            409,
            Map.of("email", "This email is already in use")
    ),
    VALIDATION_EXCEPTION(
            "VALIDATION_EXCEPTION",
            "Validation Failed",
            "Oops! Some of the data you sent doesn’t look right. Please review the fields and try again.",
            400,
            null
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    );

    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final Map<String, String> errors;
}
