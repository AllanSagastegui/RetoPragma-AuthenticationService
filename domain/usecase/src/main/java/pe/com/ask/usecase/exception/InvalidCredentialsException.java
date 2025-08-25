package pe.com.ask.usecase.exception;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super(
                "Invalid Credentials",
                "The provided credentials are incorrect",
                401
        );
    }
}