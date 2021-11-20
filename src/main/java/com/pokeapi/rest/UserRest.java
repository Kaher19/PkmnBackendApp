package com.pokeapi.rest;

import java.util.List;
import java.util.Map;

import com.pokeapi.exception.ValidationExceptionsHandler;
import com.pokeapi.model.User;
import com.pokeapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserRest {

    @Autowired
    private UserService usuarioService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<User>> obtenerUsuario() {
        List<User> usuarios = usuarioService.getUsers();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<User> getUsuario(@PathVariable Integer id) {
        User user = usuarioService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validateExceptions(MethodArgumentNotValidException ex) {
        ValidationExceptionsHandler exHandler = new ValidationExceptionsHandler(ex);
        return exHandler.handleValidationExceptions();
    }

}