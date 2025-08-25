package pe.com.ask.api.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private String tittle;
    private String message;
    private int status;
    private Date timestamp;
    private Object errors;
}