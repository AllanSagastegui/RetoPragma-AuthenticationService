package pe.com.ask.security.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class JwtException extends BaseException  {
    public JwtException() {
        super(
                ErrorCatalog.JWT_GENERIC.getErrorCode(),
                ErrorCatalog.JWT_GENERIC.getTitle(),
                ErrorCatalog.JWT_GENERIC.getMessage(),
                ErrorCatalog.JWT_GENERIC.getStatus(),
                ErrorCatalog.JWT_GENERIC.getErrors()
        );
    }
}