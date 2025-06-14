package com.ecomfsrwebflux.client_service.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.ecomfsrwebflux.client_service.entity.User;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserDao extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);
}
