package com.pokeapi.rest;

import java.util.Map;

import javax.validation.Valid;

import com.pokeapi.config.JWTTokenUtil;
import com.pokeapi.config.JwtResponse;
import com.pokeapi.exception.ValidationExceptionsHandler;
import com.pokeapi.model.Request.LoginRequest;
import com.pokeapi.model.Request.RegisterRequest;
import com.pokeapi.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;


//JWT
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthRest {

    @Autowired
	private AuthenticationManager authenticationManager;

    @Autowired
	private JWTTokenUtil jwtTokenUtil;

    @Autowired
	private UserDetailsService userDetailsService;
    
    @Autowired
    private AuthService authService;

	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> postLogin(@Valid @RequestBody LoginRequest request) throws Exception {
        String token = authService.login(request, jwtTokenUtil, userDetailsService, authenticationManager);
		return ResponseEntity.ok().body(new JwtResponse(token));
    }

	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity<?> postRegister(@Valid @RequestBody RegisterRequest request)throws Exception {
        return ResponseEntity.ok(authService.register(request));
    }

	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/logout")
    public ResponseEntity<?> postLogout(@RequestHeader("Authorization") String authToken) throws DataAccessException{
        authService.logout(authToken);
    return ResponseEntity.noContent().build();
    }

	@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/self")
    public ResponseEntity<UserDetails> getLoggedUser() {
        return ResponseEntity.status(HttpStatus.OK).body((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validateExceptions(MethodArgumentNotValidException ex) {
        ValidationExceptionsHandler exHandler = new ValidationExceptionsHandler(ex);
        return exHandler.handleValidationExceptions();
    }

}