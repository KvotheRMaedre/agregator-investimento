package com.kvothe.agregadorinvestimentos.services;


import com.kvothe.agregadorinvestimentos.dto.AccountResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.AccountStockResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.AssociateAccountStockDTO;
import com.kvothe.agregadorinvestimentos.entity.AccountStock;
import com.kvothe.agregadorinvestimentos.entity.AccountStockId;
import com.kvothe.agregadorinvestimentos.repository.AccountRepository;
import com.kvothe.agregadorinvestimentos.repository.AccountStockRepository;
import com.kvothe.agregadorinvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void associateStock(String accountId, AssociateAccountStockDTO request) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(request.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
                id,
                account,
                stock,
                request.quantity()
        );

        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDTO> getAllStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(accountStock ->
                        new AccountStockResponseDTO(
                            accountStock.getStock().getStockId(),
                            accountStock.getQuantity(),
                        0.0
                        )
                )
                .toList();
    }
}