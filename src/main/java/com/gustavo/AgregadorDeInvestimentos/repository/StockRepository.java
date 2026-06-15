package com.gustavo.AgregadorDeInvestimentos.repository;

import com.gustavo.AgregadorDeInvestimentos.entity.Account;
import com.gustavo.AgregadorDeInvestimentos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, String> {
}
