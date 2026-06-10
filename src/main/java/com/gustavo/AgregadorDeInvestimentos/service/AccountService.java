package com.gustavo.AgregadorDeInvestimentos.service;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.AssociateAccountStockDto;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStock;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStockId;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.AccountStockRepository;
import com.gustavo.AgregadorDeInvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository,
                          StockRepository stockRepository,
                          AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(String accountId, AssociateAccountStockDto dto) {

        var account = accountRepository.findById(UUID.fromString(accountId))
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
}
