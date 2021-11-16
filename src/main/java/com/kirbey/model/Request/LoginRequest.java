package com.kirbey.model.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message="Este campo es obligatorio y no puede estar vacío")
    @Size(min = 5, max = 50, message="El campo debe tener de 5 hasta 50 caracteres")
    private String username;

    @NotBlank(message="Este campo es obligatorio y no puede estar vacío")
    @Pattern(regexp="^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$&*]).{8,}$", message="Al menos 8 caracteres, una letra, un numero y un simbolo especial")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
