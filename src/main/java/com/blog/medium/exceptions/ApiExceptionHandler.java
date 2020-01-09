package com.blog.medium.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private ModelAndView getModelAndView(HttpStatus httpStatus, ErrorMessage errorMessage){
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("exceptions");
        modelAndView.setStatus(httpStatus);
        modelAndView.addObject("errorMessage",errorMessage);

        return modelAndView;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ModelAndView handleNotFoundException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(
                404,
                e.getMessage(),
                "Go to localhost:8080/swagger-ui.html#/ for the documentation",
                HttpStatus.NOT_FOUND
        );
        log.error("Not found error",e);
        return getModelAndView(httpStatus,errorMessage);
    }

    @ExceptionHandler(value = {InvalidArgumentException.class})
    public ModelAndView handleInvalidArgumentException(InvalidArgumentException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorMessage errorMessage = new ErrorMessage(
                400,
                e.getMessage(),
                "Go to localhost:8080/swagger-ui.html#/ for the documentation",
                HttpStatus.BAD_REQUEST
        );
        log.error("Invalid Arguments passed", e);
        return getModelAndView(httpStatus,errorMessage);
    }

    @ExceptionHandler(value = {GenericException.class})
    public ModelAndView handleGenericException(GenericException e){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage(
                500,
                e.getMessage(),
                "Go to localhost:8080/swagger-ui.html#/ for the documentation",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error("Some unknown exception occured, please check stack trace",e);
        return getModelAndView(httpStatus,errorMessage);
    }
}
