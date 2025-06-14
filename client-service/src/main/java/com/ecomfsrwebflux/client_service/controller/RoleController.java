package com.ecomfsrwebflux.client_service.controller;

import com.ecomfsrwebflux.client_service.dto.RoleDto;
import com.ecomfsrwebflux.client_service.dto.UserDto;
import com.ecomfsrwebflux.client_service.service.Impl.RoleServiceImpl;
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
@RequestMapping("/api/v1/roles")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    private final RoleServiceImpl roleService;

    @Autowired
    private MeterRegistry meterRegistry;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    @Timed(value = "role.findAll", description = "Time taken to retrieve all roles")
    public Flux<RoleDto> findAll() {
        long startTime = System.currentTimeMillis();
        return roleService.findAll()
                .doFinally(signal -> logAndRecordMetrics("findAll", startTime));
    }

    @GetMapping("/id/{id}")
    @Timed(value = "role.findById", description = "Time taken to retrieve role by ID")
    public Mono<RoleDto> findById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return roleService.findById(id)
                .doFinally(signal -> logAndRecordMetrics("findById", startTime));
    }

    @PostMapping("/")
    @Timed(value = "role.save", description = "Time taken to add a role")
    public Mono<RoleDto> save(@RequestBody RoleDto dto) {
        long startTime = System.currentTimeMillis();
        return roleService.save(dto)
                .doFinally(signal -> logAndRecordMetrics("saveSingle", startTime));
    }

    @PostMapping("/list/")
    @Timed(value = "role.save", description = "Time taken to add all roles")
    public Flux<RoleDto> save(@RequestBody List<RoleDto> dtos) {
        long startTime = System.currentTimeMillis();
        return roleService.save(dtos)
                .doFinally(signal -> logAndRecordMetrics("saveList", startTime));
    }

    @PutMapping("/id/{id}")
    public Mono<RoleDto> updateById(@PathVariable String id, @RequestBody RoleDto dto) {
        long startTime = System.currentTimeMillis();
        return roleService.updateById(id, dto)
                .doFinally(signal -> logAndRecordMetrics("updateById", startTime));
    }

    @DeleteMapping("/id/{id}")
    public Mono<Void> deleteById(@PathVariable String id) {
        long startTime = System.currentTimeMillis();
        return roleService.deleteById(id)
                .doFinally(signal -> logAndRecordMetrics("deleteById", startTime));
    }

    @GetMapping("/simulate")
    public Flux<String> simulateConcurrency() {
        return roleService.simulateConcurrentRoleProcessing();
    }
    private void logAndRecordMetrics(String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Operation '{}' completed in {} ms", operation, duration);
        meterRegistry.timer("role.operation.timer", "operation", operation)
                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}

