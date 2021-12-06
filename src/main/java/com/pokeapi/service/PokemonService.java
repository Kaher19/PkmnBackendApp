package com.pokeapi.service;

import java.util.List;

import com.pokeapi.exception.ConflictException;
import com.pokeapi.exception.NotFoundException;
import com.pokeapi.model.PokemonMember;
import com.pokeapi.model.User;
import com.pokeapi.repository.PokemonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pkmnRepository;

    @Autowired
    private AuthService authService;
    
    public List<PokemonMember> getPokemonTeamByUserId(Integer userId) {
        User authenticatedUser = authService.getAuthUser();
        List<PokemonMember> foundPokemons = pkmnRepository.findByUserId(authenticatedUser.getId());
        return foundPokemons;
    }

    public PokemonMember addPokemon(Integer userId, Integer pokemonId){
        PokemonMember pkmnMember = pkmnRepository.findByUserIdAndPokemonId(userId, pokemonId);
        if (pkmnMember != null)
            throw new ConflictException("El pokemon ya forma parte del equipo");
        pkmnMember = new PokemonMember();
        pkmnMember.setUserId(userId);
        pkmnMember.setPokemonId(pokemonId);
        pkmnMember = pkmnRepository.save(pkmnMember);
        return pkmnMember;
    }

    public PokemonMember removePokemon(Integer userId, Integer pokemonId){
        PokemonMember pkmnMember = pkmnRepository.findByUserIdAndPokemonId(userId, pokemonId);
        if(pkmnMember == null)
            throw new NotFoundException("El pokemon no forma parte del equipo");
        pkmnRepository.delete(pkmnMember);
        return pkmnMember;
    }
}
