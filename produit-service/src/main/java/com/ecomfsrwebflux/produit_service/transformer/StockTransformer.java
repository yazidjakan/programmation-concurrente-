package com.ecomfsrwebflux.produit_service.transformer;

import com.ecomfsrwebflux.produit_service.dto.StockGetDto;
import com.ecomfsrwebflux.produit_service.dto.StockPostDto;
import com.ecomfsrwebflux.produit_service.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockTransformer extends AbstractTransformer<Stock, StockGetDto> {
    @Override
    public Stock toEntity(StockGetDto dto) {
        if(dto == null) {
            return null;
        }else{
            Stock entity=new Stock();
            entity.setId(dto.id());
            entity.setQuantite(dto.quantite());
            entity.setStatut(dto.statut());
            entity.setProduitIds(dto.produitIds());
            return entity;
        }
    }

    @Override
    public StockGetDto toDto(Stock entity) {
        if (entity == null) {
            return null;
        }else{
            StockGetDto dto=new StockGetDto(
                    entity.getId(),
                    entity.getQuantite(),
                    entity.getStatut(),
                    entity.getProduitIds()
            );
            return dto;
        }
    }

    public Stock toEntityPost(StockPostDto dto) {
        if(dto == null) {
            return null;
        }else{
            Stock entity=new Stock();
            entity.setId(dto.id());
            entity.setQuantite(dto.quantite());
            entity.setStatut(dto.statut());
            return entity;
        }
    }

    public StockPostDto toDtoPost(Stock entity) {
        if (entity == null) {
            return null;
        }else{
            StockPostDto dto=new StockPostDto(
                    entity.getId(),
                    entity.getQuantite(),
                    entity.getStatut());
            return dto;
        }
    }
}
