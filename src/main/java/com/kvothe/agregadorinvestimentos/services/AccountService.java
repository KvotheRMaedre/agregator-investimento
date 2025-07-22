package com.kvothe.agregadorinvestimentos.services;

import com.kvothe.agregadorinvestimentos.client.BrapiClient;
import com.kvothe.agregadorinvestimentos.dto.AccountStockResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.AssociateAccountStockDTO;
import com.kvothe.agregadorinvestimentos.entity.AccountStock;
import com.kvothe.agregadorinvestimentos.entity.AccountStockId;
import com.kvothe.agregadorinvestimentos.repository.AccountRepository;
import com.kvothe.agregadorinvestimentos.repository.AccountStockRepository;
import com.kvothe.agregadorinvestimentos.repository.StockRepository;
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

    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository,
                          StockRepository stockRepository,
                          AccountStockRepository accountStockRepository,
                          BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
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
                            getTotal(accountStock.getStock().getStockId(), accountStock.getQuantity())
                        )
                )
                .toList();
    }

    private double getTotal(String stockId, Integer quantity) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();

        return quantity * price;
    }
}