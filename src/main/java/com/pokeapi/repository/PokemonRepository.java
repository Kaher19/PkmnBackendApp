package com.pokeapi.repository;

import java.util.List;

import com.pokeapi.model.PokemonMember;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends CrudRepository<PokemonMember, Integer>{
    
    List<PokemonMember> findByUserId(Integer userId);

    PokemonMember findByUserIdAndPokemonId(Integer userId, Integer pokemonId);
    
}
