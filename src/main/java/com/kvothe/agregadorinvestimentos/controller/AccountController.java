package com.kvothe.agregadorinvestimentos.controller;

import com.kvothe.agregadorinvestimentos.dto.AccountStockResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.AssociateAccountStockDTO;
import com.kvothe.agregadorinvestimentos.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId, @RequestBody AssociateAccountStockDTO request){
        accountService.associateStock(accountId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDTO>> getAllStocks(@PathVariable("accountId") String accountId){
        var stocks = accountService.getAllStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
