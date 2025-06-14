package com.ecomfsrwebflux.commande_service.transformer;

import com.ecomfsrwebflux.commande_service.dto.FactureGetDto;
import com.ecomfsrwebflux.commande_service.dto.FacturePostDto;
import com.ecomfsrwebflux.commande_service.entity.Facture;
import org.springframework.stereotype.Component;

@Component
public class FactureTransformer extends AbstractTransformer<Facture, FactureGetDto> {
    @Override
    public Facture toEntity(FactureGetDto dto) {
        if (dto == null) {
            return null;
        } else {
            Facture entity = new Facture();
            entity.setId(dto.id());
            entity.setReference(dto.reference());
            entity.setMontantTotal(dto.montantTotal());
            entity.setDateCreation(dto.dateCreation());
            entity.setCommandeId(dto.commandeId());
            entity.setStatut(dto.statut());
            return entity;
        }
    }

    @Override
    public FactureGetDto toDto(Facture entity) {
        if (entity == null) {
            return null;
        } else {
            return new FactureGetDto(
                    entity.getId(),
                    entity.getReference(),
                    entity.getMontantTotal(),
                    entity.getDateCreation(),
                    entity.getCommandeId(),
                    entity.getStatut()
            );
        }
    }

    public Facture toEntityPost(FacturePostDto dto) {
        if (dto == null) {
            return null;
        } else {
            Facture entity = new Facture();
            entity.setId(dto.id());
            entity.setMontantTotal(dto.montantTotal());
            entity.setDateCreation(dto.dateCreation());
            entity.setStatut(dto.statut());
            return entity;
        }
    }

    public FacturePostDto toDtoPost(Facture entity) {
        if (entity == null) {
            return null;
        } else {
            return new FacturePostDto(
                    entity.getId(),
                    entity.getMontantTotal(),
                    entity.getDateCreation(),
                    entity.getStatut()
            );
        }
    }
}
