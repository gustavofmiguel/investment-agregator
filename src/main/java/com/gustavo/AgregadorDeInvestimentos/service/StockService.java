package com.gustavo.AgregadorDeInvestimentos.service;


import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateStockDto;
import com.gustavo.AgregadorDeInvestimentos.entity.Stock;
import com.gustavo.AgregadorDeInvestimentos.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void createStock(CreateStockDto createStockDto) {

        var stock = new Stock(
            createStockDto.stockId(),
            createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
