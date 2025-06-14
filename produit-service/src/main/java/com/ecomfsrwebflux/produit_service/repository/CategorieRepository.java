package com.ecomfsrwebflux.produit_service.repository;

import com.ecomfsrwebflux.produit_service.entity.Categorie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategorieRepository extends ReactiveMongoRepository<Categorie, String> {

    // Trouver une catégorie par son nom
    Mono<Categorie> findByNom(String nom);
}

