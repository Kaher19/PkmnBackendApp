package com.kirbey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Existe un conflicto con la entidad");
    }

    public ConflictException(String mensaje) {
        super(mensaje);
    }
}
