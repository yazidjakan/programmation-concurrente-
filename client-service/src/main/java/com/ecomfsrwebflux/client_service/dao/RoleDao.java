package com.ecomfsrwebflux.client_service.dao;

import com.ecomfsrwebflux.client_service.entity.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface RoleDao extends ReactiveMongoRepository<Role, String> {
    Mono<Role> findByName (String name);
}
