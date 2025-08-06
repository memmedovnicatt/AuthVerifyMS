package com.nicat.authverifymicroservice.model.exception.handler;

import com.nicat.authverifymicroservice.model.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put("error", "Invalid input");
            }
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotAccess(RuntimeException ex) {
        log.error("NotAccess ->  {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFounds(RuntimeException ex) {
        log.error("NotFound ->  {}", ex.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}