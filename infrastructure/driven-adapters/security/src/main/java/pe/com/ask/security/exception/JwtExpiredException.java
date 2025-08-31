package pe.com.ask.security.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class JwtExpiredException extends BaseException {
    public JwtExpiredException() {
        super(
                ErrorCatalog.JWT_EXPIRED.getErrorCode(),
                ErrorCatalog.JWT_EXPIRED.getTitle(),
                ErrorCatalog.JWT_EXPIRED.getMessage(),
                ErrorCatalog.JWT_EXPIRED.getStatus(),
                ErrorCatalog.JWT_EXPIRED.getErrors()
        );
    }
}