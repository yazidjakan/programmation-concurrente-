package com.ecomfsrwebflux.produit_service.repository;

import com.ecomfsrwebflux.produit_service.entity.Fournisseur;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FournisseurRepository extends ReactiveMongoRepository<Fournisseur, String> {

    // Trouver un fournisseur par email
    Mono<Fournisseur> findByEmail(String email);
}

