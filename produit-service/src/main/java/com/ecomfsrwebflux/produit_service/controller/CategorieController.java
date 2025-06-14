package com.ecomfsrwebflux.produit_service.controller;

import com.ecomfsrwebflux.produit_service.dto.CategorieGetDto;
import com.ecomfsrwebflux.produit_service.dto.CategoriePostDto;
import com.ecomfsrwebflux.produit_service.service.Impl.CategorieServiceImpl;
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
@RequestMapping("/api/v1/categories")
public class CategorieController {

    private static final Logger logger = LoggerFactory.getLogger(CategorieController.class);
    private final CategorieServiceImpl categorieService;

    @Autowired
    private MeterRegistry meterRegistry;

    public CategorieController(CategorieServiceImpl categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping("/")
    @Timed(value = "categorie.findAll", description = "Time taken to retrieve all categories")
    public Flux<CategorieGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return categorieService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "categorie.findById", description = "Time taken to retrieve categorie by ID")
    public Mono<CategorieGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return categorieService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "categorie.save", description = "Time taken to add a categorie")
    public Mono<CategoriePostDto> save(@RequestBody CategoriePostDto dto) {
        long startTime = System.currentTimeMillis();
        return categorieService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "categorie.save", description = "Time taken to add all categories")
    public Flux<CategorieGetDto> save(@RequestBody List<CategorieGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return categorieService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<CategorieGetDto> updateById(@PathVariable String id, @RequestBody CategorieGetDto dto) {
        long startTime = System.currentTimeMillis();
        return categorieService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return categorieService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return categorieService.simulateConcurrentCategorieProcess();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}

