package com.ecomfsrwebflux.commande_service.transformer;

import com.ecomfsrwebflux.commande_service.dto.CommandeItemGetDto;
import com.ecomfsrwebflux.commande_service.dto.CommandeItemPostDto;
import com.ecomfsrwebflux.commande_service.entity.CommandeItem;
import org.springframework.stereotype.Component;

@Component
public class CommandeItemTransformer extends AbstractTransformer<CommandeItem, CommandeItemGetDto> {
    @Override
    public CommandeItem toEntity(CommandeItemGetDto dto) {
        if (dto == null) {
            return null;
        } else {
            CommandeItem entity = new CommandeItem();
            entity.setId(dto.id());
            entity.setProduitId(dto.produitId());
            entity.setQuantite(dto.quantite());
            entity.setCommandeId(dto.commandeId());
            return entity;
        }
    }

    @Override
    public CommandeItemGetDto toDto(CommandeItem entity) {
        if (entity == null) {
            return null;
        } else {
            return new CommandeItemGetDto(
                    entity.getId(),
                    entity.getProduitId(),
                    entity.getQuantite(),
                    entity.getCommandeId()
            );
        }
    }

    public CommandeItem toEntityPost(CommandeItemPostDto dto) {
        if (dto == null) {
            return null;
        } else {
            CommandeItem entity = new CommandeItem();
            entity.setId(dto.id());
            entity.setQuantite(dto.quantite());
            return entity;
        }
    }

    public CommandeItemPostDto toDtoPost(CommandeItem entity) {
        if (entity == null) {
            return null;
        } else {
            return new CommandeItemPostDto(
                    entity.getId(),
                    entity.getQuantite()
            );
        }
    }
}
