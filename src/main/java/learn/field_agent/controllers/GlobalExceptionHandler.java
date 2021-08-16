package learn.field_agent.controllers;

import learn.field_agent.data.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataException.class)
    public ResponseEntity<String> handleException(DataException dex) {
        return new ResponseEntity("Something went wrong with retrieving data - please try again later",
                HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity("Something went exceptionally wrong, please contact app support.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
