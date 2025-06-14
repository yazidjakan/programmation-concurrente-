package com.ecomfsrwebflux.produit_service.service.Impl;

import com.ecomfsrwebflux.produit_service.dto.StockGetDto;
import com.ecomfsrwebflux.produit_service.dto.StockPostDto;
import com.ecomfsrwebflux.produit_service.entity.Stock;
import com.ecomfsrwebflux.produit_service.repository.StockRepository;
import com.ecomfsrwebflux.produit_service.service.facade.StockService;
import com.ecomfsrwebflux.produit_service.transformer.StockTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockDao;
    private final StockTransformer stockTransformer;

    @Override
    public Mono<StockGetDto> findById(String id) {
        log.info("Fetching stock by ID: {}", id);
        return stockDao.findById(id)
                .map(stockTransformer::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to find a Stock with the given Id : " + id)));
    }

    @Override
    public Flux<StockGetDto> findAll() {
        log.info("Fetching all stocks");
        return stockDao.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("List of stocks is empty")))
                .map(stockTransformer::toDto);
    }

    public Mono<StockPostDto> save(StockPostDto dto) {
        log.info("Creating new stock with Quantite: {}", dto.quantite());

        Stock stock = stockTransformer.toEntityPost(dto);
        return stockDao.save(stock)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(stockTransformer::toDtoPost)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    @Override
    public Mono<StockGetDto> save(StockGetDto dto) {
        log.info("Creating new stock with Quantite: {}", dto.quantite());

        Stock stock = stockTransformer.toEntity(dto);
        return stockDao.save(stock)
                .doOnSuccess(c -> log.info("Successfully created commande"))
                .map(stockTransformer::toDto)
                .onErrorMap(ex -> {
                    log.error("Error occurred while creating commande", ex);
                    return new RuntimeException("An unexpected error occurred while creating the commande.", ex);
                });
    }

    @Override
    public Flux<StockGetDto> save(List<StockGetDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(stockTransformer::toEntity)
                .flatMap(stockDao::save)
                .sequential()
                .map(stockTransformer::toDto);
    }

    @Override
    public Mono<StockGetDto> updateById(String id, StockGetDto dto) {
        return findById(id)
                .flatMap(existing -> {
                    Stock updatedStock = stockTransformer.toEntity(dto);
                    updatedStock.setId(id);
                    updatedStock.setQuantite(dto.quantite());
                    updatedStock.setStatut(dto.statut());
                    updatedStock.setProduitIds(dto.produitIds());

                    log.info("Successfully updated stock with ID: {}", id);
                    return stockDao.save(updatedStock)
                            .map(stockTransformer::toDto);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting stock with ID: {}", id);
        return findById(id)
                .flatMap(existing -> stockDao.deleteById(existing.id()))
                .doOnSuccess(unused -> log.info("Successfully deleted stock with ID: {}", id));
    }

    public Flux<String> simulateConcurrentStockProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent stock " + i)
                .doOnNext(log::info);
    }
}

