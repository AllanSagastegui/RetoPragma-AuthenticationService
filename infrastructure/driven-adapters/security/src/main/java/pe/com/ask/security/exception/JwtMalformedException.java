package pe.com.ask.security.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class JwtMalformedException extends BaseException {
    public JwtMalformedException() {
        super(
                ErrorCatalog.JWT_MALFORMED.getErrorCode(),
                ErrorCatalog.JWT_MALFORMED.getTitle(),
                ErrorCatalog.JWT_MALFORMED.getMessage(),
                ErrorCatalog.JWT_MALFORMED.getStatus(),
                ErrorCatalog.JWT_MALFORMED.getErrors()
        );
    }
}