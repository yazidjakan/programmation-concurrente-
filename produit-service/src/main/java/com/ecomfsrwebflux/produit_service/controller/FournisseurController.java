package com.ecomfsrwebflux.produit_service.controller;

import com.ecomfsrwebflux.produit_service.dto.FournisseurGetDto;
import com.ecomfsrwebflux.produit_service.dto.FournisseurPostDto;
import com.ecomfsrwebflux.produit_service.service.Impl.FournisseurServiceImpl;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {

    private static final Logger logger = LoggerFactory.getLogger(FournisseurController.class);
    private final FournisseurServiceImpl fournisseurService;

    @Autowired
    private MeterRegistry meterRegistry;

    public FournisseurController(FournisseurServiceImpl fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping("/")
    @Timed(value = "fournisseur.findAll", description = "Time taken to retrieve all fournisseurs")
    public Flux<FournisseurGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return fournisseurService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "fournisseur.findById", description = "Time taken to retrieve fournisseur by ID")
    public Mono<FournisseurGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return fournisseurService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "fournisseur.save", description = "Time taken to add a fournisseur")
    public Mono<FournisseurPostDto> save(@RequestBody FournisseurPostDto dto) {
        long startTime = System.currentTimeMillis();
        return fournisseurService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "fournisseur.save", description = "Time taken to add all fournisseurs")
    public Flux<FournisseurGetDto> save(@RequestBody List<FournisseurGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return fournisseurService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<FournisseurGetDto> updateById(@PathVariable String id, @RequestBody FournisseurGetDto dto) {
        long startTime = System.currentTimeMillis();
        return fournisseurService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return fournisseurService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return fournisseurService.simulateConcurrentFournisseurProcess();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}

