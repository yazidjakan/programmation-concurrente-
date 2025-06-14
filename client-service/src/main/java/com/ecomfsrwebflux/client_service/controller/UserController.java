package com.ecomfsrwebflux.client_service.controller;

import com.ecomfsrwebflux.client_service.dto.UserDto;
import com.ecomfsrwebflux.client_service.service.Impl.UserServiceImpl;
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
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;

    @Autowired
    private MeterRegistry meterRegistry;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @Timed(value = "user.findAll", description = "Time taken to retrieve all users")
    public Flux<UserDto> findAll() {
        long startTime = System.currentTimeMillis();
        return userService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "user.findById", description = "Time taken to retrieve user by ID")
    public Mono<UserDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return userService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "user.save", description = "Time taken to add a user")
    public Mono<UserDto> save(@RequestBody UserDto dto) {
        long startTime = System.currentTimeMillis();
        return userService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "user.save", description = "Time taken to add all users")
    public Flux<UserDto> save(@RequestBody List<UserDto> dtos) {
        long startTime = System.currentTimeMillis();
        return userService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<UserDto> updateById(@PathVariable String id, @RequestBody UserDto dto) {
        long startTime = System.currentTimeMillis();
        return userService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return userService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }
    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return userService.simulateConcurrentUserProcess();
    }

    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("user.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
