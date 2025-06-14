package com.ecomfsrwebflux.client_service.dao;

import com.ecomfsrwebflux.client_service.entity.Commande;
import com.ecomfsrwebflux.client_service.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommandeDao extends ReactiveMongoRepository<Commande, String> {
}
