package pe.com.ask.model.baseexception.errors;

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
            "A user with this information already exists. Please use different credentials.",
            409,
            Map.of("email or National ID", "This email or National ID is already in use")
    ),
    VALIDATION_EXCEPTION(
            "AUTH_VALIDATION_EXCEPTION",
            "Validation Failed",
            "Oops! Some of the data you sent doesn’t look right. Please review the fields and try again.",
            400,
            null
    ),
    INTERNAL_SERVER_ERROR(
            "AUTH_INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    ),
    JWT_EXPIRED(
            "AUTH_JWT_EXPIRED",
            "JWT Expired",
            "Your session has expired. Please log in again.",
            401,
            Map.of("token", "The provided JWT has expired")
    ),
    JWT_INVALID_SIGNATURE(
            "AUTH_JWT_INVALID_SIGNATURE",
            "Invalid JWT Signature",
            "The signature of the provided JWT is invalid.",
            401,
            Map.of("token", "Invalid token signature")
    ),
    JWT_MALFORMED(
            "AUTH_JWT_MALFORMED",
            "Malformed JWT",
            "The provided JWT is malformed or corrupted.",
            400,
            Map.of("token", "Malformed JWT")
    ),
    JWT_GENERIC(
            "AUTH_JWT_GENERIC",
            "JWT Error",
            "An error occurred while processing the JWT.",
            401,
            Map.of("token", "Generic JWT processing error")
    ),
    UNAUTHORIZED(
            "AUTH_UNAUTHORIZED",
            "Unauthorized",
            "You are not authenticated. Please log in to access this resource.",
            401,
            Map.of("user", "Authentication required")
    ),

    ACCESS_DENIED(
            "AUTH_ACCESS_DENIED",
            "Access Denied",
            "You do not have permission to access this resource.",
            403,
            Map.of("user", "You lack the necessary permissions")
    );

    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final Map<String, String> errors;
}
