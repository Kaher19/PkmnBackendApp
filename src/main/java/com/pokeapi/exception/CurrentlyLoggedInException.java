package com.pokeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_MODIFIED)
public class CurrentlyLoggedInException extends RuntimeException {
    
    public CurrentlyLoggedInException(){
        super("Ya tienes una sesión activa");
    }

    public CurrentlyLoggedInException(String mensaje){
        super(mensaje);
    }

}
