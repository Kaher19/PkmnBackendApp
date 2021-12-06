package com.pokeapi.model.Request;

import javax.validation.constraints.NotBlank;

public class PokemonMemberRequest {

    @NotBlank(message="Este campo es obligatorio y no puede estar vacío")
    private Integer pokemonId;

    public Integer getPokemonId(){
        return this.pokemonId;
    }
}
