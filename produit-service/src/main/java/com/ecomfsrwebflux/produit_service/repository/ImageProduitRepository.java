package com.ecomfsrwebflux.produit_service.repository;

import com.ecomfsrwebflux.produit_service.entity.ImageProduit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ImageProduitRepository extends ReactiveMongoRepository<ImageProduit, String> {

    // Trouver toutes les images liées à un produit par son ID
    Flux<ImageProduit> findByProduitId(String produitId);
}

