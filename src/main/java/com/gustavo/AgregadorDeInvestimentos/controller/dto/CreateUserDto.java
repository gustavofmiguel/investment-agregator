package com.gustavo.AgregadorDeInvestimentos.controller.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "Usuário é obrigatório")
        String username,

        @Email(message = "Email Inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 3, message = "Senha deve ter no mínimo X Caracteres") // exemplo
        String password) { }
