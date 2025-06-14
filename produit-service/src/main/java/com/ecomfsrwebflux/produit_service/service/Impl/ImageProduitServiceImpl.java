package com.ecomfsrwebflux.produit_service.service.Impl;

import com.ecomfsrwebflux.produit_service.dto.ImageProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ImageProduitPostDto;
import com.ecomfsrwebflux.produit_service.entity.ImageProduit;
import com.ecomfsrwebflux.produit_service.repository.ImageProduitRepository;
import com.ecomfsrwebflux.produit_service.repository.ProduitRepository;
import com.ecomfsrwebflux.produit_service.service.facade.ImageProduitService;
import com.ecomfsrwebflux.produit_service.transformer.ImageProduitTransformer;
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
public class ImageProduitServiceImpl implements ImageProduitService {

    private final ImageProduitRepository imageProduitRepository;
    private final ImageProduitTransformer transformer;
    private final ProduitRepository produitRepository;

    @Override
    public Mono<ImageProduitGetDto> findById(String id) {
        log.info("Fetching image produit by ID: {}", id);
        return imageProduitRepository.findById(id)
                .map(transformer::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to find image produit with ID: " + id)))
                .doOnError(e -> log.error("Image produit not found with ID: {}", id, e));
    }

    @Override
    public Flux<ImageProduitGetDto> findAll() {
        return imageProduitRepository.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("List of image produits is empty")))
                .map(transformer::toDto);
    }

    @Override
    public Mono<ImageProduitGetDto> save(ImageProduitGetDto dto) {
        ImageProduit imageProduit = transformer.toEntity(dto);
        return produitRepository.findById(dto.produitId())
                .switchIfEmpty(Mono.error(new RuntimeException("Produit not found with ID: " + dto.produitId())))
                .flatMap(produit -> {
                    imageProduit.setProduitId(produit.getId());
                    return imageProduitRepository.save(imageProduit);
                })
                .map(transformer::toDto)
                .doOnSuccess(saved -> log.info("Successfully saved image produit"))
                .doOnError(e -> log.error("Error saving image produit", e));
    }

    @Override
    public Flux<ImageProduitGetDto> save(List<ImageProduitGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(transformer::toEntity)
                .flatMap(imageProduitRepository::save)
                .sequential()
                .map(transformer::toDto);
    }

    public Mono<ImageProduitPostDto> save(ImageProduitPostDto dto) {
        ImageProduit imageProduit = transformer.toEntityPost(dto);
        return imageProduitRepository.save(imageProduit)
                .map(transformer::toDtoPost)
                .doOnSuccess(saved -> log.info("Successfully saved image produit (POST DTO)"))
                .doOnError(e -> log.error("Error saving image produit (POST DTO)", e));
    }

    public Mono<ImageProduitGetDto> updateById(String id, ImageProduitGetDto dto) {
        return imageProduitRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Image produit not found with ID: " + id)))
                .flatMap(existing -> {
                    ImageProduit updated = transformer.toEntity(dto);
                    updated.setId(id);
                    updated.setUrl(dto.url());
                    updated.setProduitId(dto.produitId()); // Assure-toi que le produit est bien géré
                    return imageProduitRepository.save(updated);
                })
                .map(transformer::toDto)
                .doOnSuccess(updated -> log.info("Successfully updated image produit with ID: {}", id))
                .doOnError(e -> log.error("Error updating image produit with ID: {}", id, e));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting image produit with ID: {}", id);
        return imageProduitRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Image produit not found with ID: " + id)))
                .flatMap(found -> imageProduitRepository.deleteById(id))
                .doOnSuccess(unused -> log.info("Successfully deleted image produit with ID: {}", id))
                .doOnError(e -> log.error("Error deleting image produit with ID: {}", id, e));
    }

    public Flux<String> simulateConcurrentImageProduitProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent image produit " + i)
                .doOnNext(log::info);
    }
}

