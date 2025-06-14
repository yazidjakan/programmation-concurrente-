package com.ecomfsrwebflux.client_service.service.Impl;

import com.ecomfsrwebflux.client_service.dao.CommandeDao;
import com.ecomfsrwebflux.client_service.dao.UserDao;
import com.ecomfsrwebflux.client_service.dto.CommandeDto.CommandeGetDto;
import com.ecomfsrwebflux.client_service.dto.CommandeDto.CommandePostDto;
import com.ecomfsrwebflux.client_service.entity.Commande;
import com.ecomfsrwebflux.client_service.enums.EtatCommande;
import com.ecomfsrwebflux.client_service.service.facade.CommandeService;
import com.ecomfsrwebflux.client_service.transformer.CommandeTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeServiceImpl implements CommandeService {

    private final CommandeDao commandeDao;
    private final UserDao userDao;
    private final CommandeTransformer commandeTransformer;

    @Override
    public Mono<CommandeGetDto> findById(String id) {
        log.info("Fetching commande by ID: {}", id);
        return commandeDao.findById(id)
                .map(commandeTransformer::toDto)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Commande not found with ID: {}", id);
                    return Mono.error(new RuntimeException("Unable to find a commande with the given ID: " + id));
                }));
    }

    @Override
    public Flux<CommandeGetDto> findAll() {
        return commandeDao.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("List of commandes is empty")))
                .map(commandeTransformer::toDto);
    }

    @Override
    public Mono<CommandeGetDto> save(CommandeGetDto dto) {
        Commande commande = commandeTransformer.toEntity(dto);
        commande.setEtatCommande(EtatCommande.SOUMISE);

        return commandeDao.save(commande)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(commandeTransformer::toDto)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    @Override
    public Flux<CommandeGetDto> save(List<CommandeGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .map(commandeTransformer::toEntity)
                .doOnNext(c -> c.setEtatCommande(EtatCommande.SOUMISE))
                .collectList()
                .flatMapMany(commandeDao::saveAll)
                .map(commandeTransformer::toDto)
                .doOnComplete(() -> log.info("Successfully saved {} commandes", dtos.size()))
                .onErrorResume(ex -> {
                    log.error("Error occurred while saving commandes", ex);
                    return Flux.error(new RuntimeException("An unexpected error occurred while saving commandes.", ex));
                });
    }


    public Mono<CommandePostDto> save(CommandePostDto dto) {
        Commande commande = commandeTransformer.toEntityPost(dto);
        commande.setEtatCommande(EtatCommande.SOUMISE);

        return commandeDao.save(commande)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(commandeTransformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    @Override
    public Mono<CommandeGetDto> updateById(String id, CommandeGetDto dto) {
        return commandeDao.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Commande not found with id: " + id)))
                .flatMap(existing -> {
                    Commande updated = commandeTransformer.toEntity(dto);
                    updated.setId(existing.getId()); // conserve le même ID
                    return commandeDao.save(updated)
                            .doOnSuccess(c -> log.info("Successfully updated commande with ID: {}", id))
                            .map(commandeTransformer::toDto);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(c -> {
                    log.info("Deleting commande with ID: {}", id);
                    return commandeDao.deleteById(id)
                            .doOnSuccess(v -> log.info("Successfully deleted commande with ID: {}", id));
                });
    }
}
