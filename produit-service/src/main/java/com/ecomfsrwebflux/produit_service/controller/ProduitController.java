package com.ecomfsrwebflux.produit_service.controller;

import com.ecomfsrwebflux.produit_service.dto.ProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ProduitPostDto;
import com.ecomfsrwebflux.produit_service.service.Impl.ProduitServiceImpl;
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
@RequestMapping("/api/v1/produits")
public class ProduitController {

    private static final Logger logger = LoggerFactory.getLogger(ProduitController.class);
    private final ProduitServiceImpl ProduitService;

    @Autowired
    private MeterRegistry meterRegistry;

    public ProduitController(ProduitServiceImpl ProduitService) {
        this.ProduitService = ProduitService;
    }

    @GetMapping("/")
    @Timed(value = "Produit.findAll", description = "Time taken to retrieve all Produits")
    public Flux<ProduitGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return ProduitService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "Produit.findById", description = "Time taken to retrieve Produit by ID")
    public Mono<ProduitGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return ProduitService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "Produit.save", description = "Time taken to add a Produit")
    public Mono<ProduitPostDto> save(@RequestBody ProduitPostDto dto) {
        long startTime = System.currentTimeMillis();
        return ProduitService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "Produit.save", description = "Time taken to add all Produits")
    public Flux<ProduitGetDto> save(@RequestBody List<ProduitGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return ProduitService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<ProduitGetDto> updateById(@PathVariable String id, @RequestBody ProduitGetDto dto) {
        long startTime = System.currentTimeMillis();
        return ProduitService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return ProduitService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return ProduitService.simulateConcurrentProduitProcess();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}


