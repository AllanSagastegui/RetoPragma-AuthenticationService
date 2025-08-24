package pe.com.ask.api.exception.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ErrorResponse {
    private String tittle;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Object errors;
}