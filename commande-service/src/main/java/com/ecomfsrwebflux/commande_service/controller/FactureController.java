package com.ecomfsrwebflux.commande_service.controller;

import com.ecomfsrwebflux.commande_service.dto.FactureGetDto;
import com.ecomfsrwebflux.commande_service.dto.FacturePostDto;
import com.ecomfsrwebflux.commande_service.service.Impl.FactureServiceImpl;
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
@RequestMapping("/api/v1/factures")
public class FactureController {

    private static final Logger logger = LoggerFactory.getLogger(FactureController.class);
    private final FactureServiceImpl factureService;

    @Autowired
    private MeterRegistry meterRegistry;

    public FactureController(FactureServiceImpl factureService) {
        this.factureService = factureService;
    }

    @GetMapping("/")
    @Timed(value = "facture.findAll", description = "Time taken to retrieve all factures")
    public Flux<FactureGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return factureService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "facture.findById", description = "Time taken to retrieve facture by ID")
    public Mono<FactureGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return factureService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "facture.save", description = "Time taken to save facture")
    public Mono<FacturePostDto> save(@RequestBody FacturePostDto dto) {
        long startTime = System.currentTimeMillis();
        return factureService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "facture.saveList", description = "Time taken to save all factures")
    public Flux<FactureGetDto> save(@RequestBody List<FactureGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return factureService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<FactureGetDto> updateById(@PathVariable String id, @RequestBody FactureGetDto dto) {
        long startTime = System.currentTimeMillis();
        return factureService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return factureService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return factureService.simulateConcurrentFactures();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("facture.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}

