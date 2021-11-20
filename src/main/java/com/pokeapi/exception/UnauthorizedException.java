package com.pokeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("No tiene los permisos para relizar esta acción");
    }

    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }
    
}
