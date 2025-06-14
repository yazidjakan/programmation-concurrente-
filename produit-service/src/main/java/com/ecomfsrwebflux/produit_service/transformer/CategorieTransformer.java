package com.ecomfsrwebflux.produit_service.transformer;

import com.ecomfsrwebflux.produit_service.dto.CategorieGetDto;
import com.ecomfsrwebflux.produit_service.dto.CategoriePostDto;
import com.ecomfsrwebflux.produit_service.entity.Categorie;
import org.springframework.stereotype.Component;

@Component
public class CategorieTransformer extends AbstractTransformer<Categorie, CategorieGetDto> {
    @Override
    public Categorie toEntity(CategorieGetDto dto) {
        if(dto == null) {
            return null;
        }else{
            Categorie entity=new Categorie();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            entity.setNom(dto.description());
            entity.setProduitIds(dto.produitIds());
            return entity;
        }
    }

    @Override
    public CategorieGetDto toDto(Categorie entity) {
        if (entity == null) {
            return null;
        }else{
            CategorieGetDto dto=new CategorieGetDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getDescription(),
                    entity.getProduitIds()
            );
            return dto;
        }
    }

    public Categorie toEntityPost(CategoriePostDto dto) {
        if(dto == null) {
            return null;
        }else{
            Categorie entity=new Categorie();
            entity.setId(dto.id());
            entity.setId(dto.nom());
            entity.setNom(dto.description());
            return entity;
        }
    }

    public CategoriePostDto toDtoPost(Categorie entity) {
        if (entity == null) {
            return null;
        }else{
            CategoriePostDto dto=new CategoriePostDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getDescription()
            );
            return dto;
        }
    }


}
