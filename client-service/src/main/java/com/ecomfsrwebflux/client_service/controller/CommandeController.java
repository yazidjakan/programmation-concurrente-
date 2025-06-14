package com.ecomfsrwebflux.client_service.controller;

import com.ecomfsrwebflux.client_service.dto.CommandeDto.CommandeGetDto;
import com.ecomfsrwebflux.client_service.dto.CommandeDto.CommandePostDto;
import com.ecomfsrwebflux.client_service.dto.RoleDto;
import com.ecomfsrwebflux.client_service.service.Impl.CommandeServiceImpl;
import com.ecomfsrwebflux.client_service.service.Impl.RoleServiceImpl;
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
@RequestMapping("/api/v1/commandes")
public class CommandeController {

    private static final Logger logger = LoggerFactory.getLogger(CommandeController.class);
    private final CommandeServiceImpl commandeService;

    @Autowired
    private MeterRegistry meterRegistry;

    public CommandeController(CommandeServiceImpl commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping("/")
    @Timed(value = "commande.findAll", description = "Time taken to retrieve all commandes")
    public Flux<CommandeGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return commandeService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "commande.findById", description = "Time taken to retrieve commande by ID")
    public Mono<CommandeGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return commandeService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "commande.save", description = "Time taken to add a commande")
    public Mono<CommandePostDto> save(@RequestBody CommandePostDto dto) {
        long startTime = System.currentTimeMillis();
        return commandeService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "commande.save", description = "Time taken to add all commandes")
    public Flux<CommandeGetDto> save(@RequestBody List<CommandeGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return commandeService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<CommandeGetDto> updateById(@PathVariable String id, @RequestBody CommandeGetDto dto) {
        long startTime = System.currentTimeMillis();
        return commandeService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return commandeService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}


