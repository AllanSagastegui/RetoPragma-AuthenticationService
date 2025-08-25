package pe.com.ask.usecase.exception;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException() {
        super(
                "Role Not Found",
                "The specified role does not exist",
                404
        );
    }
}