package com.kvothe.agregadorinvestimentos.controller;

import com.kvothe.agregadorinvestimentos.dto.StockDTO;
import com.kvothe.agregadorinvestimentos.entity.Stock;
import com.kvothe.agregadorinvestimentos.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<String> createStock(@RequestBody StockDTO request){
        var stockId = stockService.createStock(request);

        return ResponseEntity.created(URI.create("/v1/stocks/" + stockId)).build();
    }
}
