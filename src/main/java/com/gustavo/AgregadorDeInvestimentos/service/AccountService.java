package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.client.BrapiClient;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.AccountStockResponseDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.AssociateAccountStockDto;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStock;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStockId;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountStockRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
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

    public void associateStock(UUID accountId, AssociateAccountStockDto dto) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockResponseDto(
                        as.getStock().getStockId(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(), as.getStock().getStockId())
                ))
                .toList();
    }

    private double getTotal(Integer quantity, String stockId) {
        try {
            var response = brapiClient.getQuote(TOKEN, stockId);
            if (response.results() == null || response.results().isEmpty()){
                return 0.0;
            }
            return quantity * response.results().getFirst().regularMarketPrice();
        } catch (Exception e) {
            return 0.0;
        }
    }
}
