package com.ecomfsrwebflux.produit_service.service.facade;

import com.ecomfsrwebflux.produit_service.dto.ProduitGetDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProduitService extends AbstractService<ProduitGetDto, String>{
    public Flux<ProduitGetDto> findByFournisseurId(String fournisseurId);
    public Flux<ProduitGetDto> findByCategorieId(String categorieId);
}
