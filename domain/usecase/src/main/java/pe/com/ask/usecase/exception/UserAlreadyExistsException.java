package pe.com.ask.usecase.exception;

import pe.com.ask.usecase.utils.ErrorCatalog;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException() {
        super(
                ErrorCatalog.USER_ALREADY_EXISTS.getErrorCode(),
                ErrorCatalog.USER_ALREADY_EXISTS.getTitle(),
                ErrorCatalog.USER_ALREADY_EXISTS.getMessage(),
                ErrorCatalog.USER_ALREADY_EXISTS.getStatus()
        );
    }
}