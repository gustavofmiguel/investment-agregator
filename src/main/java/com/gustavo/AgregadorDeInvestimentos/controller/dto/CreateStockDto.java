package com.gustavo.AgregadorDeInvestimentos.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateStockDto(

        @NotBlank(message = "Stock ID é obrigatório")
        String stockId,

        @NotBlank(message = "Descrição  é obrigatória")
        String description) { }
