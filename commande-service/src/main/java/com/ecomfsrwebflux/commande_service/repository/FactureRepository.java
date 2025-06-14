package com.ecomfsrwebflux.commande_service.repository;

import com.ecomfsrwebflux.commande_service.entity.Facture;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends ReactiveMongoRepository<Facture, String> {
}
