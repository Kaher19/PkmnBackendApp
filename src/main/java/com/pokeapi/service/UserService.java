package com.pokeapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.pokeapi.exception.ConflictException;
import com.pokeapi.exception.NotFoundException;
import com.pokeapi.model.User;
import com.pokeapi.model.Request.RegisterRequest;
import com.pokeapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent())
            throw new NotFoundException("No se encontró un usuario con id " +id);
        return user.get();
    }

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent())
            throw new NotFoundException("No se encontró un usuario con username " +username);
        return user.get();
    }
    
    public List<User> getUsers() {
        List<User> users = new LinkedList<>();
        userRepository.findAll().iterator().forEachRemaining(users::add);
        return users;
    }

    @Transactional
    public User create(RegisterRequest request) throws ConflictException {
        /*
            Temporalmente asignamos por defecto nuestro proveedor como primera
            cuenta y el tipo de rol "Usuario".
        */
        if(existUsername(request.getUsername())){
            throw new ConflictException("El username " +request.getUsername() +" ya se encuentra en los registros.");
        }
        if(existEmail(request.getEmail())){
            throw new ConflictException("El email "+request.getEmail() +" ya se encuentra en los registros.");
        }

        User registeredUser = new User();
        registeredUser.setUsername(request.getUsername());
        registeredUser.setPassword(passwordEncoder.encode(request.getPassword()));
        registeredUser.setEmail(request.getEmail());
        registeredUser = userRepository.save(registeredUser);
       
        return registeredUser;
    }

    public void updateUser(String username){

    }

    @Transactional
    public void updateTokenAt(String username, Timestamp timeS){
        User user = getUserByUsername(username);
        user.setTokenAt(timeS);
        userRepository.save(user);
    }

    /**
     * Nos permite saber si el token de acceso se creó con un tiempo mayor a la validez del token
     * @param username - el username del usuario de quien obtendremos el token 
     * @param hours - tiempo máximo por el cual el token es valido 
     * @return <code>True</code> en caso de que sea mayor a la duración de validez de un token, o
     * el campo sea <code>Null</code>. <code>False</code> de otro modo.
     */
    public Boolean isTokenOldGenerated(String username, Integer hours){
        Timestamp tokenGeneratedTime = getUserByUsername(username).getTokenAt();
        if(tokenGeneratedTime == null) return false;
        Long msGeneratedTime = tokenGeneratedTime.getTime();
        return hoursDiference( System.currentTimeMillis(), msGeneratedTime) < hours;
    }

    /**
     * Comprueba si un usuario existe
     * @param username El nombre de usuario a buscar.
     * @return <code>True</code> si existe el usuario, <code>False</code> en caso contrario.
     */
    public Boolean existUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    
    public Boolean existEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    private Long hoursDiference(Long time1, Long time2){
        time1 = (time1/(1000*60*60));//Obetenemos las horas del timestamp
        time2 = (time2/(1000*60*60));//Obetenemos las horas del timestamp
        return Math.abs(time2-time1);
    }
}