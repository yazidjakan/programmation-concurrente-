package com.ecomfsrwebflux.client_service.service.Impl;

import com.ecomfsrwebflux.client_service.dao.RoleDao;
import com.ecomfsrwebflux.client_service.dto.RoleDto;
import com.ecomfsrwebflux.client_service.dto.UserDto;
import com.ecomfsrwebflux.client_service.entity.Role;
import com.ecomfsrwebflux.client_service.entity.User;
import com.ecomfsrwebflux.client_service.service.facade.RoleService;
import com.ecomfsrwebflux.client_service.transformer.RoleTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final RoleTransformer roleTransformer;

    @Override
    public Mono<RoleDto> findById(String id) {
        log.info("Fetching role by ID: {}", id);
        return roleDao.findById(id)
                .map(roleTransformer::toDto)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Role not found with ID: {}", id);
                    return Mono.error(new RuntimeException("Unable to find a role with the given Id: " + id));
                }));
    }

    @Override
    public Flux<RoleDto> findAll() {
        return roleDao.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("List of roles is empty")))
                .map(roleTransformer::toDto);
    }

    @Override
    public Mono<RoleDto> save(RoleDto dto) {
        log.info("Creating new role with name: {}", dto.name());
        return roleDao.save(roleTransformer.toEntity(dto))
                .map(roleTransformer::toDto);
    }

    @Override
    public Flux<RoleDto> save(List<RoleDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel()) // ✅ traitement concurrent pour chaque sauvegarde de rôle
                .flatMap(dto -> roleDao.save(roleTransformer.toEntity(dto)))
                .sequential()
                .map(roleTransformer::toDto);
    }

    @Override
    public Mono<RoleDto> updateById(String id, RoleDto dto) {
        return findById(id)
                .flatMap(existingDto -> {
                    Role updated = roleTransformer.toEntity(dto);
                    updated.setId(id);
                    log.info("Successfully updated role with ID: {}", id);
                    return roleDao.save(updated)
                            .map(roleTransformer::toDto);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(role -> {
                    log.info("Deleting role with ID: {}", id);
                    return roleDao.deleteById(id)
                            .doOnSuccess(v -> log.info("Successfully deleted role with ID: {}", id));
                });
    }

    // ✅ Démonstration de traitement concurrent simulé (pour la soutenance)
    public Flux<String> simulateConcurrentRoleProcessing() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent de rôle " + i)
                .doOnNext(log::info);
    }
}
