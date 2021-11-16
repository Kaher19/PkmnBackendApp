package com.kirbey.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    @JsonIgnore
    @Column(name="token_at")
    private Timestamp tokenAt;

    public User() {}

    public User id(Integer id) {
        this.id = id;
        return this;
    }

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    
    /**
     * @param token the token to set
     */
    public void setTokenAt(Timestamp tokenAt) {
        this.tokenAt = tokenAt;
    }

    /**
     * @return the token
     */
    public Timestamp getTokenAt() {
        return tokenAt;
    }

    public String toString(){
        return "\n{\n\"id:\""+ getId() +",\n\"username:\""+ getUsername()+
        ",\n\"email:\""+ getEmail()+"\"\n}\n";
    }
    
}