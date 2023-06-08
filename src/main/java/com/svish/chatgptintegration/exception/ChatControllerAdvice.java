package com.svish.chatgptintegration.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ChatControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MissingAuthorizationException.class)
    public ResponseEntity<ErrorDetails> handleMissingAuthorizationException(
            MissingAuthorizationException exception
    ) {
        ErrorDetails details = ErrorDetails.builder()
                .errorMessage(exception.getMessage())
                .statusCode(exception.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(details, exception.getHttpStatus());
    }

}
