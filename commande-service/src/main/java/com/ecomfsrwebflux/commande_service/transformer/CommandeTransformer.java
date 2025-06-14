package com.ecomfsrwebflux.commande_service.transformer;

import com.ecomfsrwebflux.commande_service.dto.CommandeGetDto;
import com.ecomfsrwebflux.commande_service.dto.CommandePostDto;
import com.ecomfsrwebflux.commande_service.entity.Commande;
import org.springframework.stereotype.Component;

@Component
public class CommandeTransformer extends AbstractTransformer<Commande, CommandeGetDto> {
    @Override
    public Commande toEntity(CommandeGetDto dto) {
        if (dto == null) {
            return null;
        } else {
            Commande entity = new Commande();
            entity.setId(dto.id());
            entity.setUserId(dto.userId());
            entity.setEtatCommande(dto.etatCommande());
            return entity;
        }
    }

    @Override
    public CommandeGetDto toDto(Commande entity) {
        if (entity == null) {
            return null;
        } else {
            CommandeGetDto dto = new CommandeGetDto(
                    entity.getId(),
                    entity.getUserId(),
                    entity.getEtatCommande()
            );
            return dto;
        }
    }

    public Commande toEntityPost(CommandePostDto dto) {
        if (dto == null) {
            return null;
        } else {
            Commande entity = new Commande();
            entity.setId(dto.id());
            entity.setEtatCommande(dto.etatCommande());
            return entity;
        }
    }

    public CommandePostDto toDtoPost(Commande entity) {
        if (entity == null) {
            return null;
        } else {
            return new CommandePostDto(
                    entity.getId(),
                    entity.getEtatCommande()
            );
        }
    }
}
