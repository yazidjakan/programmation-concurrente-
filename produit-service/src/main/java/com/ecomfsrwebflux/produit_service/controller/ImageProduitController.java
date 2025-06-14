package com.ecomfsrwebflux.produit_service.controller;

import com.ecomfsrwebflux.produit_service.dto.ImageProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ImageProduitPostDto;
import com.ecomfsrwebflux.produit_service.service.Impl.ImageProduitServiceImpl;
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
@RequestMapping("/api/v1/image-produits")
public class ImageProduitController {

    private static final Logger logger = LoggerFactory.getLogger(ImageProduitController.class);
    private final ImageProduitServiceImpl ImageProduitService;

    @Autowired
    private MeterRegistry meterRegistry;

    public ImageProduitController(ImageProduitServiceImpl ImageProduitService) {
        this.ImageProduitService = ImageProduitService;
    }

    @GetMapping("/")
    @Timed(value = "ImageProduit.findAll", description = "Time taken to retrieve all ImageProduits")
    public Flux<ImageProduitGetDto> findAll() {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "ImageProduit.findById", description = "Time taken to retrieve ImageProduit by ID")
    public Mono<ImageProduitGetDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "ImageProduit.save", description = "Time taken to add a ImageProduit")
    public Mono<ImageProduitPostDto> save(@RequestBody ImageProduitPostDto dto) {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "ImageProduit.save", description = "Time taken to add all ImageProduits")
    public Flux<ImageProduitGetDto> save(@RequestBody List<ImageProduitGetDto> dtos) {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<ImageProduitGetDto> updateById(@PathVariable String id, @RequestBody ImageProduitGetDto dto) {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return ImageProduitService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return ImageProduitService.simulateConcurrentImageProduitProcess();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("commande.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}


