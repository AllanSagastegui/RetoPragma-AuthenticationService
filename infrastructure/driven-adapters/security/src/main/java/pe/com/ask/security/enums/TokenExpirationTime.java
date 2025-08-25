package pe.com.ask.security.enums;

import lombok.Getter;

@Getter
public enum TokenExpirationTime {
    ACCESS(86400);

    private final long seconds;

    TokenExpirationTime(long seconds) {
        this.seconds = seconds;
    }
}