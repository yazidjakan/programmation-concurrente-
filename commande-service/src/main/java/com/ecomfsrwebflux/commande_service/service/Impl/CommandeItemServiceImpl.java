package com.ecomfsrwebflux.commande_service.service.Impl;

import com.ecomfsrwebflux.commande_service.dto.CommandeItemGetDto;
import com.ecomfsrwebflux.commande_service.dto.CommandeItemPostDto;
import com.ecomfsrwebflux.commande_service.entity.CommandeItem;
import com.ecomfsrwebflux.commande_service.repository.CommandeItemRepository;
import com.ecomfsrwebflux.commande_service.service.facade.CommandeItemService;
import com.ecomfsrwebflux.commande_service.transformer.CommandeItemTransformer;
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
public class CommandeItemServiceImpl implements CommandeItemService {

    private final CommandeItemRepository commandeItemRepository;
    private final CommandeItemTransformer transformer;

    @Override
    public Mono<CommandeItemGetDto> findById(String id) {
        log.info("Fetching CommandeItem by ID: {}", id);
        return commandeItemRepository.findById(id)
                .map(transformer::toDto)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("CommandeItem not found with ID: {}", id);
                    return Mono.error(new RuntimeException("CommandeItem not found with ID: " + id));
                }));
    }

    @Override
    public Flux<CommandeItemGetDto> findAll() {
        return commandeItemRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No CommandeItems found")))
                .map(transformer::toDto);
    }

    @Override
    public Mono<CommandeItemGetDto> save(CommandeItemGetDto dto) {
        CommandeItem entity = transformer.toEntity(dto);
        return commandeItemRepository.save(entity)
                .doOnSuccess(c -> log.info("Successfully created CommandeItem"))
                .map(transformer::toDto)
                .onErrorMap(ex -> {
                    log.error("Error creating CommandeItem", ex);
                    return new RuntimeException("Failed to create CommandeItem", ex);
                });
    }
    public Mono<CommandeItemPostDto> save(CommandeItemPostDto dto) {
        CommandeItem entity = transformer.toEntityPost(dto);
        return commandeItemRepository.save(entity)
                .doOnSuccess(c -> log.info("Successfully created CommandeItem"))
                .map(transformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error creating CommandeItem", ex);
                    return new RuntimeException("Failed to create CommandeItem", ex);
                });
    }

    @Override
    public Flux<CommandeItemGetDto> save(List<CommandeItemGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(commandeItemRepository::save)
                .sequential()
                .map(transformer::toDto);
    }

    @Override
    public Mono<CommandeItemGetDto> updateById(String id, CommandeItemGetDto dto) {
        return commandeItemRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("CommandeItem not found with ID: " + id)))
                .flatMap(existing -> {
                    CommandeItem updated = transformer.toEntity(dto);
                    updated.setId(existing.getId());
                    return commandeItemRepository.save(updated)
                            .doOnSuccess(c -> log.info("Updated CommandeItem with ID: {}", id))
                            .map(transformer::toDto);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(dto -> {
                    log.info("Deleting CommandeItem with ID: {}", id);
                    return commandeItemRepository.deleteById(id)
                            .doOnSuccess(v -> log.info("Deleted CommandeItem with ID: {}", id));
                });
    }

    public Flux<String> simulateConcurrentCommandeItems() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent utilisateur " + i)
                .doOnNext(log::info);
    }
}

