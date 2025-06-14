package com.ecomfsrwebflux.produit_service.service.facade;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AbstractService <D,I>{
    Flux<D> findAll();
    Mono<D> findById(I id);
    Mono<D> save(D dto);
    Flux<D> save(List<D> dtos);
    Mono<D> updateById(I id,D dto);
    Mono<Void> deleteById(I id);
}