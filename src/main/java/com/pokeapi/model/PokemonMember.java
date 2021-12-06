package com.pokeapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pokemon_teams")
public class PokemonMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="user_id")
    private Integer userId;
    
    @Column(name="pokemon_id")
    private Integer pokemonId;

    public Integer getId(){
        return this.id;
    }

    public Integer getUserId(){
        return this.userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getPokemonId(){
        return this.pokemonId;
    }

    public void setPokemonId(Integer pokemonId){
        this.pokemonId = pokemonId;
    }
}