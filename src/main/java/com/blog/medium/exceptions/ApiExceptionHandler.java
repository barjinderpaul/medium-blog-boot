package com.blog.medium.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage(
                404,
                e.getMessage(),
                "Go to /swagger-ui.html#/ for the documentation",
                HttpStatus.BAD_REQUEST
        );
        log.error("Not found error",e);
        return new ResponseEntity<>(errorMessage,httpStatus);
    }
}
