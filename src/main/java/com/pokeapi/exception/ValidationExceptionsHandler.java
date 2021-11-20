package com.pokeapi.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

public class ValidationExceptionsHandler {
    private MethodArgumentNotValidException exception;

    public ValidationExceptionsHandler(MethodArgumentNotValidException exception){
        this.exception = exception;
    }

    public Map<String, String> handleValidationExceptions(
        ) {
            Map<String, String> errors = new HashMap<>();
            exception.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
}
