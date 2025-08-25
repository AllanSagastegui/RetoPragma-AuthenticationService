package pe.com.ask.usecase.exception;

import pe.com.ask.usecase.utils.ErrorCatalog;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(
                ErrorCatalog.USER_NOT_FOUND.getErrorCode(),
                ErrorCatalog.USER_NOT_FOUND.getTitle(),
                ErrorCatalog.USER_NOT_FOUND.getMessage(),
                ErrorCatalog.USER_NOT_FOUND.getStatus()
        );
    }
}
