package com.ecomfsrwebflux.produit_service.controller;

import com.ecomfsrwebflux.produit_service.dto.StockGetDto;
import com.ecomfsrwebflux.produit_service.dto.StockPostDto;
import com.ecomfsrwebflux.produit_service.service.Impl.StockServiceImpl;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    private final StockServiceImpl StockService;

    @Autowired
    private MeterRegistry meterRegistry;

    public StockController(StockServiceImpl StockService) {
        this.StockService = StockService;
    }

    @GetMapping("/")
    @Timed(value = "Stock.findAll", description = "Time taken to retrieve all Stocks")
    public Flux<StockGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return StockService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "Stock.findById", description = "Time taken to retrieve Stock by ID")
    public Mono<StockGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return StockService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "Stock.save", description = "Time taken to add a Stock")
    public Mono<StockPostDto> save(@RequestBody StockPostDto dto) {
        long startTime = System.currentTimeMillis();
        return StockService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "Stock.save", description = "Time taken to add all Stocks")
    public Flux<StockGetDto> save(@RequestBody List<StockGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return StockService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<StockGetDto> updateById(@PathVariable String id, @RequestBody StockGetDto dto) {
        long startTime = System.currentTimeMillis();
        return StockService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return StockService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return StockService.simulateConcurrentStockProcess();
    }
    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}


