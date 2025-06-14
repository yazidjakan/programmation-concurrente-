package com.ecomfsrwebflux.produit_service.transformer;

import com.ecomfsrwebflux.produit_service.dto.FournisseurGetDto;
import com.ecomfsrwebflux.produit_service.dto.FournisseurPostDto;
import com.ecomfsrwebflux.produit_service.entity.Fournisseur;
import org.springframework.stereotype.Component;

@Component
public class FournisseurTransformer extends AbstractTransformer<Fournisseur, FournisseurGetDto> {
    @Override
    public Fournisseur toEntity(FournisseurGetDto dto) {
        if(dto == null) {
            return null;
        }else{
            Fournisseur entity=new Fournisseur();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            entity.setAdresse(dto.adresse());
            entity.setTelephone(dto.telephone());
            entity.setEmail(dto.email());
            entity.setProduitIds(dto.produitIds());
            return entity;
        }
    }

    @Override
    public FournisseurGetDto toDto(Fournisseur entity) {
        if (entity == null) {
            return null;
        }else{
            FournisseurGetDto dto=new FournisseurGetDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getAdresse(),
                    entity.getTelephone(),
                    entity.getEmail(),
                    entity.getProduitIds()
            );
            return dto;
        }
    }

    public Fournisseur toEntityPost(FournisseurPostDto dto) {
        if(dto == null) {
            return null;
        }else{
            Fournisseur entity=new Fournisseur();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            entity.setAdresse(dto.adresse());
            entity.setTelephone(dto.telephone());
            entity.setEmail(dto.email());
            return entity;
        }
    }

    public FournisseurPostDto toDtoPost(Fournisseur entity) {
        if (entity == null) {
            return null;
        }else{
            FournisseurPostDto dto=new FournisseurPostDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getAdresse(),
                    entity.getTelephone(),
                    entity.getEmail()
            );
            return dto;
        }
    }
}
