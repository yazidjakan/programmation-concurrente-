package com.ecomfsrwebflux.commande_service.repository;

import com.ecomfsrwebflux.commande_service.entity.CommandeItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeItemRepository extends ReactiveMongoRepository<CommandeItem, String> {
}
