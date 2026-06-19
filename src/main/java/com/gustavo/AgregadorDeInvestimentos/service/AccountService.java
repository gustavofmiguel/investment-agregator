package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.client.BrapiClient;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.AccountStockResponseDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.AssociateAccountStockDto;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStock;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStockId;
import com.gustavo.AgregadorDeInvestimentos.exception.ResourceNotFoundException;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountStockRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {

    @Value("${TOKEN}")
    private String TOKEN;
    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;
    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository,
                          StockRepository stockRepository,
                          AccountStockRepository accountStockRepository,
                          BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    @Transactional
    public void associateStock(UUID accountId, AssociateAccountStockDto dto) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada: " + accountId));

        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResourceNotFoundException("Ação não encontrada: " + dto.stockId()));

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );
        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStocks(UUID accountId) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada: " + accountId));

        var accountStocks = account.getAccountStocks();
        if (accountStocks.isEmpty()) {
            return List.of();
        }

        var tickers = accountStocks.stream()
                .map(as -> as.getStock().getStockId())
                .toList();
        var priceMap = fetchPrices(tickers);

        return accountStocks.stream()
                .map(as -> {
                    var stockId = as.getStock().getStockId();
                    var price = priceMap.getOrDefault(stockId, 0.0);
                    return new AccountStockResponseDto(stockId, as.getQuantity(), as.getQuantity() * price);
                })
                .toList();
    }

    private Map<String, Double> fetchPrices(List<String> tickers) {
        Map<String, Double> priceMap = new java.util.HashMap<>();
        for (String ticker : tickers) {
            try {
                var response = brapiClient.getQuote(TOKEN, ticker);
                if (response.results() != null && !response.results().isEmpty()) {
                    var stock = response.results().getFirst();
                    priceMap.put(ticker, stock.regularMarketPrice());
                }
            } catch (Exception e) {
                System.out.println("Erro BRAPI para " + ticker + ": " + e.getMessage());
                priceMap.put(ticker, 0.0);
            }
        }
        return priceMap;
    }
}
