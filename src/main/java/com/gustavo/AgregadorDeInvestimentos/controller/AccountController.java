package com.gustavo.AgregadorDeInvestimentos.controller;

import com.gustavo.AgregadorDeInvestimentos.controller.dto.AccountStockResponseDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.AssociateAccountStockDto;
import com.gustavo.AgregadorDeInvestimentos.controller.dto.CreateAccountDto;
import com.gustavo.AgregadorDeInvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                               @RequestBody AssociateAccountStockDto dto) {
        accountService.associateStock(accountId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> associateStock(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
