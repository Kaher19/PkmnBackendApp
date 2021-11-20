package com.pokeapi.rest;

import java.util.Map;

import com.pokeapi.exception.ValidationExceptionsHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")

public class PokemonRest {

    @Autowired
    private RestTemplate restT;

    private String baseUrl = "https://pokeapi.co/api/v2";
    
	@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/pokemon/{id}")
    public ResponseEntity<Object> getPokemon(@PathVariable Integer id) {
        String targetUrl = baseUrl+"/pokemon/"+id;
        Object pokemon = this.restT.getForObject(targetUrl, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemon/*Poner aqui lo que devuelve*/);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/item/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Integer id) {
        String targetUrl = baseUrl+"/item/"+id;
        Object pokemon = this.restT.getForObject(targetUrl, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemon/*Poner aqui lo que devuelve*/);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/region")
    public ResponseEntity<Object> getRegion() {
        String targetUrl = baseUrl+"/region";
        Object pokemon = this.restT.getForObject(targetUrl, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemon/*Poner aqui lo que devuelve*/);
    }

    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/berry")
    public ResponseEntity<Object> getBerry(@RequestParam Integer limit ) {
        String targetUrl = baseUrl+"/berry?&limit="+limit;
        Object pokemon = this.restT.getForObject(targetUrl, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemon/*Poner aqui lo que devuelve*/);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validateExceptions(MethodArgumentNotValidException ex) {
        ValidationExceptionsHandler exHandler = new ValidationExceptionsHandler(ex);
        return exHandler.handleValidationExceptions();
    }
}
