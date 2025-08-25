package pe.com.ask.usecase.exception;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String email) {
        super(
                "User already exists",
                "User with email " + email + " already exists",
                409
        );
    }
}