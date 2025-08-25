package pe.com.ask.usecase.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseException extends RuntimeException {
    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    protected BaseException(String errorCode, String title, String message, int status) {
        super(message);
        this.errorCode = errorCode;
        this.title = title;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}