package com.ecomfsrwebflux.produit_service.service.Impl;

import com.ecomfsrwebflux.produit_service.dto.ProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ProduitPostDto;
import com.ecomfsrwebflux.produit_service.entity.Categorie;
import com.ecomfsrwebflux.produit_service.entity.Produit;
import com.ecomfsrwebflux.produit_service.repository.CategorieRepository;
import com.ecomfsrwebflux.produit_service.repository.FournisseurRepository;
import com.ecomfsrwebflux.produit_service.repository.ProduitRepository;
import com.ecomfsrwebflux.produit_service.service.facade.ProduitService;
import com.ecomfsrwebflux.produit_service.transformer.ProduitTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitTransformer transformer;

    @Override
    public Mono<ProduitGetDto> save(ProduitGetDto dto) {
        Produit produit = transformer.toEntity(dto);
        return produitRepository.save(produit)
                .map(transformer::toDto);
    }

    @Override
    public Flux<ProduitGetDto> save(List<ProduitGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(produitRepository::save)
                .sequential()
                .map(transformer::toDto);
    }


    public Mono<ProduitPostDto> save(ProduitPostDto dto) {
        Produit produit = transformer.toEntityPost(dto);
        return produitRepository.save(produit)
                .doOnSuccess(c -> log.info("Successfully created produit"))
                .map(transformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating produit", ex);
                    return new RuntimeException("An unexpected error occurred while creating the produit.", ex);
                });
    }

    @Override
    public Mono<ProduitGetDto> updateById(String id, ProduitGetDto dto) {
        return produitRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Produit not found")))
                .flatMap(existing -> {
                    Produit updated = transformer.toEntity(dto);
                    updated.setId(id);
                    return produitRepository.save(updated);
                }).map(transformer::toDto);
    }

    @Override
    public Mono<ProduitGetDto> findById(String id) {
        return produitRepository.findById(id)
                .map(transformer::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Produit not found")));
    }

    @Override
    public Flux<ProduitGetDto> findAll() {
        return produitRepository.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("List of produits is empty")))
                .map(transformer::toDto);
    }

    @Override
    public Flux<ProduitGetDto> findByCategorieId(String categorieId) {
        return produitRepository.findByCategorieId(categorieId)
                .map(transformer::toDto);
    }

    @Override
    public Flux<ProduitGetDto> findByFournisseurId(String fournisseurId) {
        return produitRepository.findByFournisseurId(fournisseurId)
                .map(transformer::toDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return produitRepository.deleteById(id);
    }
    public Flux<String> simulateConcurrentProduitProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent produit " + i)
                .doOnNext(log::info);
    }


}

