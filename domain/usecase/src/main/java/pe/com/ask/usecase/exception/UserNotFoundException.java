package pe.com.ask.usecase.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(
                "User Not Found",
                "The specified user does not exist",
                404
        );
    }
}
