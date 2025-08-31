package pe.com.ask.usecase.exception;

import pe.com.ask.model.baseexception.BaseException;
import pe.com.ask.model.baseexception.errors.ErrorCatalog;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException() {

        super(
                ErrorCatalog.ROLE_NOT_FOUND.getErrorCode(),
                ErrorCatalog.ROLE_NOT_FOUND.getTitle(),
                ErrorCatalog.ROLE_NOT_FOUND.getMessage(),
                ErrorCatalog.ROLE_NOT_FOUND.getStatus(),
                ErrorCatalog.ROLE_NOT_FOUND.getErrors()
        );
    }
}