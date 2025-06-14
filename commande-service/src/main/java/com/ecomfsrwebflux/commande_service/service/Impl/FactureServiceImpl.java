package com.ecomfsrwebflux.commande_service.service.Impl;

import com.ecomfsrwebflux.commande_service.dto.FactureGetDto;
import com.ecomfsrwebflux.commande_service.dto.FacturePostDto;
import com.ecomfsrwebflux.commande_service.entity.Facture;
import com.ecomfsrwebflux.commande_service.repository.FactureRepository;
import com.ecomfsrwebflux.commande_service.service.facade.FactureService;
import com.ecomfsrwebflux.commande_service.transformer.FactureTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final FactureTransformer transformer;

    @Override
    public Mono<FactureGetDto> findById(String id) {
        log.info("Fetching Facture by ID: {}", id);
        return factureRepository.findById(id)
                .map(transformer::toDto)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Facture not found with ID: {}", id);
                    return Mono.error(new RuntimeException("Facture not found with ID: " + id));
                }));
    }

    @Override
    public Flux<FactureGetDto> findAll() {
        return factureRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No Factures found")))
                .map(transformer::toDto);
    }

    @Override
    public Mono<FactureGetDto> save(FactureGetDto dto) {
        Facture entity = transformer.toEntity(dto);
        return factureRepository.save(entity)
                .doOnSuccess(f -> log.info("Successfully created Facture"))
                .map(transformer::toDto)
                .onErrorMap(ex -> {
                    log.error("Error creating Facture", ex);
                    return new RuntimeException("Failed to create Facture", ex);
                });
    }

    public Mono<FacturePostDto> save(FacturePostDto dto) {
        // Injecter la date actuelle
        FacturePostDto dtoWithDate = new FacturePostDto(
                dto.id(),
                dto.montantTotal(),
                LocalDateTime.now(), // Ajoute la date de création ici
                dto.statut()
        );

        Facture entity = transformer.toEntityPost(dtoWithDate);
        return factureRepository.save(entity)
                .doOnSuccess(f -> log.info("Successfully created Facture"))
                .map(transformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error creating Facture", ex);
                    return new RuntimeException("Failed to create Facture", ex);
                });
    }


    @Override
    public Flux<FactureGetDto> save(List<FactureGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(factureRepository::save)
                .sequential()
                .map(transformer::toDto);
    }

    @Override
    public Mono<FactureGetDto> updateById(String id, FactureGetDto dto) {
        return factureRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Facture not found with ID: " + id)))
                .flatMap(existing -> {
                    Facture updated = transformer.toEntity(dto);
                    updated.setId(existing.getId());
                    return factureRepository.save(updated)
                            .doOnSuccess(f -> log.info("Updated Facture with ID: {}", id))
                            .map(transformer::toDto);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(dto -> {
                    log.info("Deleting Facture with ID: {}", id);
                    return factureRepository.deleteById(id)
                            .doOnSuccess(v -> log.info("Deleted Facture with ID: {}", id));
                });
    }

    public Flux<String> simulateConcurrentFactures() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent utilisateur " + i)
                .doOnNext(log::info);
    }
}

