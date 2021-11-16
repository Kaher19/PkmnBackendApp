package com.kirbey.service;

import java.sql.Timestamp;
import java.util.ArrayList;

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

import com.kirbey.config.JWTTokenUtil;
import com.kirbey.config.JwtTokenBlacklist;
import com.kirbey.exception.CurrentlyLoggedInException;
import com.kirbey.model.User;
import com.kirbey.model.Request.LoginRequest;
import com.kirbey.model.Request.RegisterRequest;
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
    public String login(LoginRequest request, String userAgent, JWTTokenUtil jwtTokenUtil,
            UserDetailsService userDetailsService, AuthenticationManager authenticationManager)
            throws AuthenticationException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (userService.isTokenOldGenerated(userDetails.getUsername(), 5)) {
            throw new CurrentlyLoggedInException("Ya existe una sesión activa");
        }
        // emailService.sendEmail("Se ha iniciado sesion desde: " +userAgent,
        // user.getEmail(), "Inicio de sesión");
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