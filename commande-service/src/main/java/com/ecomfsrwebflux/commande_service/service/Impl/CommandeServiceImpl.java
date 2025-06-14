package com.ecomfsrwebflux.commande_service.service.Impl;

import com.ecomfsrwebflux.commande_service.dto.CommandeGetDto;
import com.ecomfsrwebflux.commande_service.dto.CommandePostDto;
import com.ecomfsrwebflux.commande_service.entity.Commande;
import com.ecomfsrwebflux.commande_service.enums.EtatCommande;
import com.ecomfsrwebflux.commande_service.repository.CommandeRepository;
import com.ecomfsrwebflux.commande_service.service.facade.CommandeService;
import com.ecomfsrwebflux.commande_service.transformer.CommandeTransformer;
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
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository repository;
    private final CommandeTransformer transformer;

    @Override
    public Flux<CommandeGetDto> findAll() {
        return repository.findAll()
                .map(transformer::toDto);
    }

    @Override
    public Mono<CommandeGetDto> findById(String id) {
        return repository.findById(id)
                .map(transformer::toDto);
    }

    @Override
    public Mono<CommandeGetDto> save(CommandeGetDto dto) {
        Commande commande = transformer.toEntity(dto);
        commande.setEtatCommande(EtatCommande.SOUMISE);

        return repository.save(commande)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(transformer::toDto)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    public Mono<CommandePostDto> save(CommandePostDto dto) {
        Commande commande = transformer.toEntityPost(dto);
        commande.setEtatCommande(EtatCommande.SOUMISE);

        return repository.save(commande)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(transformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    @Override
    public Flux<CommandeGetDto> save(List<CommandeGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(repository::save)
                .sequential()
                .map(transformer::toDto);
    }

    @Override
    public Mono<CommandeGetDto> updateById(String id, CommandeGetDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Commande not found")))
                .flatMap(existing -> {
                    Commande updated = transformer.toEntity(dto);
                    updated.setId(id);
                    return repository.save(updated);
                })
                .map(transformer::toDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }


    public Flux<String> simulateConcurrentCommandes() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Commande traitée en parallèle n° " + i)
                .doOnNext(log::info);
    }
}
