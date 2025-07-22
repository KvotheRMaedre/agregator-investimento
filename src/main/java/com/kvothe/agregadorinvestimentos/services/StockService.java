package com.kvothe.agregadorinvestimentos.services;


import com.kvothe.agregadorinvestimentos.dto.StockDTO;
import com.kvothe.agregadorinvestimentos.entity.Stock;
import com.kvothe.agregadorinvestimentos.repository.StockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public String createStock(StockDTO stockDTO) {
        var stock = new Stock(
                stockDTO.stockId(),
                stockDTO.description()
        );
        var savedStock = stockRepository.save(stock);
        return savedStock.getStockId();
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}