package com.gustavo.AgregadorDeInvestimentos.repository;

import com.gustavo.AgregadorDeInvestimentos.entity.Account;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStock;
import com.gustavo.AgregadorDeInvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
