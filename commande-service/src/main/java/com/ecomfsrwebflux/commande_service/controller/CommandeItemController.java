package com.ecomfsrwebflux.commande_service.controller;

import com.ecomfsrwebflux.commande_service.dto.CommandeItemGetDto;
import com.ecomfsrwebflux.commande_service.dto.CommandeItemPostDto;
import com.ecomfsrwebflux.commande_service.service.Impl.CommandeItemServiceImpl;
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
@RequestMapping("/api/v1/commande-items")
public class CommandeItemController {

    private static final Logger logger = LoggerFactory.getLogger(CommandeItemController.class);
    private final CommandeItemServiceImpl commandeItemService;

    @Autowired
    private MeterRegistry meterRegistry;

    public CommandeItemController(CommandeItemServiceImpl commandeItemService) {
        this.commandeItemService = commandeItemService;
    }

    @GetMapping("/")
    @Timed(value = "commandeItem.findAll", description = "Time taken to retrieve all commande items")
    public Flux<CommandeItemGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return commandeItemService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "commandeItem.findById", description = "Time taken to retrieve commande item by ID")
    public Mono<CommandeItemGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return commandeItemService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "commandeItem.save", description = "Time taken to save commande item")
    public Mono<CommandeItemPostDto> save(@RequestBody CommandeItemPostDto dto) {
        long startTime = System.currentTimeMillis();
        return commandeItemService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "commandeItem.saveList", description = "Time taken to save all commande items")
    public Flux<CommandeItemGetDto> save(@RequestBody List<CommandeItemGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return commandeItemService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<CommandeItemGetDto> updateById(@PathVariable String id, @RequestBody CommandeItemGetDto dto) {
        long startTime = System.currentTimeMillis();
        return commandeItemService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return commandeItemService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return commandeItemService.simulateConcurrentCommandeItems();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commandeItem.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}

