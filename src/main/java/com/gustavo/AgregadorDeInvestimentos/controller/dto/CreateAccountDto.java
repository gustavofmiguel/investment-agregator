package com.gustavo.AgregadorDeInvestimentos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountDto(

        @NotBlank(message = "Descrição é obrigatória")
        String description,

        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotNull(message = "Número é obrigatório")
        Integer number) { }
