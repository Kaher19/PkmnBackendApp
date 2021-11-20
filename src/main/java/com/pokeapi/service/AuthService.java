package com.pokeapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.pokeapi.config.JWTTokenUtil;
import com.pokeapi.config.JwtTokenBlacklist;
import com.pokeapi.exception.CurrentlyLoggedInException;
import com.pokeapi.model.User;
import com.pokeapi.model.Request.LoginRequest;
import com.pokeapi.model.Request.RegisterRequest;

import org.springframework.transaction.annotation.Transactional;

//import io.jsonwebtoken.lang.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

//Seguridad con JWT
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
@Service
public class AuthService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenBlacklist tokenBlackList;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public User getAuthUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        return user;
    }

    @Transactional
    public User register(RegisterRequest request) {
        User registeredUser = userService.create(request);
        return registeredUser;
    }

    @Transactional
    public String login(LoginRequest request, JWTTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService, AuthenticationManager authenticationManager)
            throws AuthenticationException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (userService.isTokenOldGenerated(userDetails.getUsername(), 5)) {
            throw new CurrentlyLoggedInException("Ya existe una sesión activa");
        }
        userService.updateTokenAt(request.getUsername(), new Timestamp(System.currentTimeMillis()));
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Transactional
    public void logout(String token) throws DataAccessException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.updateTokenAt(user.getUsername(), null);
        tokenBlackList.addTokenToBlacklist(token);
    }
}