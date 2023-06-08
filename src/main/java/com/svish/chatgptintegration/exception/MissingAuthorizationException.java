package com.svish.chatgptintegration.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Data
public class MissingAuthorizationException extends RuntimeException {

    private HttpStatus httpStatus;

    public MissingAuthorizationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MissingAuthorizationException that = (MissingAuthorizationException) o;
        return httpStatus == that.httpStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus);
    }
}
