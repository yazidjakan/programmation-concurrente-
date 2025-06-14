package com.ecomfsrwebflux.produit_service.repository;

import com.ecomfsrwebflux.produit_service.entity.Produit;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProduitRepository extends ReactiveMongoRepository<Produit, String> {

    // Trouver les produits par catégorie
    Flux<Produit> findByCategorieId(String categorieId);

    // Trouver les produits par fournisseur
    Flux<Produit> findByFournisseurId(String fournisseurId);

    // Trouver les produits par fourchette de prix
    Flux<Produit> findByPrixBetween(Double minPrix, Double maxPrix);
}

