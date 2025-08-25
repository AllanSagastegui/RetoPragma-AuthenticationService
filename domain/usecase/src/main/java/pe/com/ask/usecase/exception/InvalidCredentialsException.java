package pe.com.ask.usecase.exception;

import pe.com.ask.usecase.utils.ErrorCatalog;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super(
                ErrorCatalog.INVALID_CREDENTIALS.getErrorCode(),
                ErrorCatalog.INVALID_CREDENTIALS.getTitle(),
                ErrorCatalog.INVALID_CREDENTIALS.getMessage(),
                ErrorCatalog.INVALID_CREDENTIALS.getStatus(),
                ErrorCatalog.INVALID_CREDENTIALS.getErrors()
        );
    }
}