package com.gustavo.AgregadorDeInvestimentos.client.dto;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateStockDto;
import com.gustavo.AgregadorDeInvestimentos.entity.Stock;

import java.util.List;

public record BrapiResponseDto(List<StockDto> results) {
}
