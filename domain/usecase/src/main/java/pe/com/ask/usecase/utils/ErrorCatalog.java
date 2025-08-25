package pe.com.ask.usecase.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCatalog {

    INVALID_CREDENTIALS(
            "AUTH-INVALID-CREDENTIALS",
                "Invalid Credentials",
            "The provided credentials are incorrect",
            401
    ),
    ROLE_NOT_FOUND(
            "AUTH-ROLE-NOT-FOUND",
            "Role Not Found",
            "The specified role does not exist",
            404
    ),
    USER_ALREADY_EXISTS(
            "AUTH-USER-ALREADY-EXISTS",
            "User already exists",
            "User with the provided email already exists",
            409
    );


    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;

}