package com.kvothe.agregadorinvestimentos.repository;

import com.kvothe.agregadorinvestimentos.entity.AccountStock;
import com.kvothe.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
