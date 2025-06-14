package com.ecomfsrwebflux.produit_service.service.Impl;

import com.ecomfsrwebflux.produit_service.dto.CategorieGetDto;
import com.ecomfsrwebflux.produit_service.dto.CategoriePostDto;
import com.ecomfsrwebflux.produit_service.entity.Categorie;
import com.ecomfsrwebflux.produit_service.repository.CategorieRepository;
import com.ecomfsrwebflux.produit_service.service.facade.CategorieService;
import com.ecomfsrwebflux.produit_service.service.facade.FournisseurService;
import com.ecomfsrwebflux.produit_service.transformer.CategorieTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private static final Logger log = LoggerFactory.getLogger(CategorieService.class);

    private final CategorieRepository categorieRepository;
    private final CategorieTransformer transformer;

    public CategorieServiceImpl(CategorieRepository categorieRepository, CategorieTransformer transformer) {
        this.categorieRepository = categorieRepository;
        this.transformer = transformer;
    }

    // Sauvegarde via GetDto (rare, mais gardé pour l'exemple)
    @Override
    public Mono<CategorieGetDto> save(CategorieGetDto dto) {
        Categorie categorie = transformer.toEntity(dto);
        return categorieRepository.save(categorie)
                .map(transformer::toDto);
    }

    @Override
    public Flux<CategorieGetDto> save(List<CategorieGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(categorieRepository::save)
                .sequential()
                .map(transformer::toDto);
    }

    // Sauvegarde via PostDto
    public Mono<CategoriePostDto> save(CategoriePostDto dto) {
        Categorie categorie = transformer.toEntityPost(dto);
        return categorieRepository.save(categorie)
                .map(transformer::toDtoPost);
    }

    @Override
    public Mono<CategorieGetDto> updateById(String id, CategorieGetDto dto) {
        return categorieRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Commande not found")))
                .flatMap(existing -> {
                    Categorie updated = transformer.toEntity(dto);
                    updated.setId(id);
                    return categorieRepository.save(updated);
                }).map(transformer::toDto);
    }

    @Override
    public Mono<CategorieGetDto> findById(String id) {
        return categorieRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Categorie not found")))
                .map(transformer::toDto);
    }

    @Override
    public Flux<CategorieGetDto> findAll() {
        return categorieRepository.findAll()
                .map(transformer::toDto);
    }

    public Flux<String> simulateConcurrentCategorieProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent categorie " + i)
                .doOnNext(log::info);
    }
    @Override
    public Mono<Void> deleteById(String id) {
        return categorieRepository.deleteById(id);
    }
}

