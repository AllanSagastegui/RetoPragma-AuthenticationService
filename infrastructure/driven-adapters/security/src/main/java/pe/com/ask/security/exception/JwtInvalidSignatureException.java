package pe.com.ask.security.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class JwtInvalidSignatureException extends BaseException  {
    public JwtInvalidSignatureException() {
        super(
                ErrorCatalog.JWT_INVALID_SIGNATURE.getErrorCode(),
                ErrorCatalog.JWT_INVALID_SIGNATURE.getTitle(),
                ErrorCatalog.JWT_INVALID_SIGNATURE.getMessage(),
                ErrorCatalog.JWT_INVALID_SIGNATURE.getStatus(),
                ErrorCatalog.JWT_INVALID_SIGNATURE.getErrors()
        );
    }
}