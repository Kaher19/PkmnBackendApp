package com.kirbey.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.kirbey.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    
    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

}