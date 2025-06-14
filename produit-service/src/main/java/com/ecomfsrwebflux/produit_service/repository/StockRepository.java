package com.ecomfsrwebflux.produit_service.repository;

import com.ecomfsrwebflux.produit_service.entity.Stock;
import com.ecomfsrwebflux.produit_service.enums.StatutStock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StockRepository extends ReactiveMongoRepository<Stock, String> {

    // ⚠️ Requiert que Stock contienne une liste de produitIds : List<String> produitIds
    Mono<Stock> findByProduitIdsContains(String produitId);

    // Filtrer les stocks par statut
    Flux<Stock> findByStatut(StatutStock statut);
}

