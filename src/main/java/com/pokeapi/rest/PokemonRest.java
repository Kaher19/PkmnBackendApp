package com.pokeapi.rest;

import java.util.List;
import java.util.Map;

import com.pokeapi.exception.ValidationExceptionsHandler;
import com.pokeapi.model.PokemonMember;
import com.pokeapi.model.User;
import com.pokeapi.model.Request.PokemonMemberRequest;
import com.pokeapi.service.AuthService;
import com.pokeapi.service.PokemonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private AuthService authService;
    
    @Autowired
    private PokemonService pokemonService;

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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/my-team")
    public ResponseEntity<Object> getMyPokemons( ) {
        User authenticatedUser = authService.getAuthUser();
        List<PokemonMember> myPokemons = pokemonService.getPokemonTeamByUserId(authenticatedUser.getId());

        return ResponseEntity.ok().body(myPokemons);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/my-team")
    public ResponseEntity<Object> addPokemon( @RequestBody PokemonMemberRequest request ) {
        User authenticatedUser = authService.getAuthUser();
        PokemonMember newMember = pokemonService.addPokemon(authenticatedUser.getId(), request.getPokemonId());

        return ResponseEntity.ok().body(newMember);
    }

    
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/my-team")
    public ResponseEntity<Object> removePokemon( @RequestBody PokemonMemberRequest request ) {
        User authenticatedUser = authService.getAuthUser();
        PokemonMember toDeleteMember = pokemonService.removePokemon(authenticatedUser.getId(), request.getPokemonId());

        return ResponseEntity.ok().body(toDeleteMember);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validateExceptions(MethodArgumentNotValidException ex) {
        ValidationExceptionsHandler exHandler = new ValidationExceptionsHandler(ex);
        return exHandler.handleValidationExceptions();
    }
}
