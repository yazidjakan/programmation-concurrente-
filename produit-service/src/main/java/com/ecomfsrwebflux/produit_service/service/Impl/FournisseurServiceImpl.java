package com.ecomfsrwebflux.produit_service.service.Impl;

import com.ecomfsrwebflux.produit_service.dto.FournisseurGetDto;
import com.ecomfsrwebflux.produit_service.dto.FournisseurPostDto;
import com.ecomfsrwebflux.produit_service.entity.Fournisseur;
import com.ecomfsrwebflux.produit_service.repository.FournisseurRepository;
import com.ecomfsrwebflux.produit_service.service.facade.FournisseurService;
import com.ecomfsrwebflux.produit_service.transformer.FournisseurTransformer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FournisseurServiceImpl implements FournisseurService {

    private static final Logger log = LoggerFactory.getLogger(FournisseurService.class);

    private final FournisseurRepository fournisseurDao;
    private final FournisseurTransformer fournisseurTransformer;

    @Override
    public Mono<FournisseurGetDto> findById(String id) {
        log.info("Fetching Fournisseur by ID: {}", id);
        return fournisseurDao.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to find a Fournisseur with the given Id : " + id)))
                .map(fournisseurTransformer::toDto)
                .doOnError(ex -> log.error("Fournisseur not found with ID: {}", id, ex));
    }

    @Override
    public Flux<FournisseurGetDto> findAll() {
        log.info("Fetching all Fournisseurs");
        return fournisseurDao.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("List of fournisseurs is Empty")))
                .map(fournisseurTransformer::toDto);
    }

    public Mono<FournisseurPostDto> save(FournisseurPostDto dto) {
        log.info("Creating new Fournisseur with name: {}", dto.nom());
        Fournisseur fournisseur = fournisseurTransformer.toEntityPost(dto);
        return fournisseurDao.save(fournisseur)
                .map(fournisseurTransformer::toDtoPost)
                .doOnSuccess(f -> log.info("Successfully created Fournisseur with name: {}", dto.nom()))
                .doOnError(ex -> log.error("Error occurred while creating Fournisseur with name: {}", dto.nom(), ex));
    }

    @Override
    public Mono<FournisseurGetDto> save(FournisseurGetDto dto) {
        log.info("Creating new Fournisseur with name: {}", dto.nom());
        Fournisseur fournisseur = fournisseurTransformer.toEntity(dto);
        return fournisseurDao.save(fournisseur)
                .map(fournisseurTransformer::toDto)
                .doOnSuccess(f -> log.info("Successfully created Fournisseur with name: {}", dto.nom()))
                .doOnError(ex -> log.error("Error occurred while creating Fournisseur with name: {}", dto.nom(), ex));
    }

    @Override
    public Flux<FournisseurGetDto> save(List<FournisseurGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(fournisseurTransformer::toEntity)
                .flatMap(fournisseurDao::save)
                .sequential()
                .map(fournisseurTransformer::toDto);
    }

    @Override
    public Mono<FournisseurGetDto> updateById(String id, FournisseurGetDto dto) {
        log.info("Updating Fournisseur with ID: {}", id);
        return fournisseurDao.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Fournisseur not found with ID: " + id)))
                .flatMap(existingFournisseur -> {
                    // Mise à jour des champs
                    existingFournisseur.setNom(dto.nom());
                    existingFournisseur.setAdresse(dto.adresse());
                    existingFournisseur.setTelephone(dto.telephone());
                    existingFournisseur.setEmail(dto.email());
                    if (dto.produitIds() != null) {
                        existingFournisseur.setProduitIds(dto.produitIds());
                    }
                    return fournisseurDao.save(existingFournisseur);
                })
                .map(fournisseurTransformer::toDto)
                .doOnSuccess(f -> log.info("Successfully updated Fournisseur with ID: {}", id))
                .doOnError(ex -> log.error("Error updating Fournisseur with ID: {}", id, ex));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting Fournisseur with ID: {}", id);
        return fournisseurDao.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Fournisseur not found with ID: " + id)))
                .flatMap(f -> fournisseurDao.deleteById(id))
                .doOnSuccess(unused -> log.info("Successfully deleted Fournisseur with ID: {}", id))
                .doOnError(ex -> log.error("Error deleting Fournisseur with ID: {}", id, ex));
    }
    public Flux<String> simulateConcurrentFournisseurProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent fournisseur " + i)
                .doOnNext(log::info);
    }
}

