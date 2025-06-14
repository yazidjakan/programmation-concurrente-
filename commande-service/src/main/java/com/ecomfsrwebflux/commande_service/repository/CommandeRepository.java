package com.ecomfsrwebflux.commande_service.repository;

import com.ecomfsrwebflux.commande_service.entity.Commande;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends ReactiveMongoRepository<Commande, String> {
}
