package com.ecomfsrwebflux.produit_service.transformer;

import com.ecomfsrwebflux.produit_service.dto.ProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ProduitPostDto;
import com.ecomfsrwebflux.produit_service.entity.Produit;
import org.springframework.stereotype.Component;

@Component
public class ProduitTransformer extends AbstractTransformer<Produit, ProduitGetDto> {
    @Override
    public Produit toEntity(ProduitGetDto dto) {
        if(dto == null) {
            return null;
        }else{
            Produit entity=new Produit();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            entity.setDescription(dto.description());
            entity.setPrix(dto.prix());
            entity.setQuantite(dto.quantite());
            entity.setImage(dto.image());
            entity.setEtatProduit(dto.etatProduit());
            entity.setCategorieId(dto.categorieId());
            entity.setFournisseurId(dto.fournisseurId());
            entity.setStockId(dto.stockId());
            return entity;
        }
    }

    @Override
    public ProduitGetDto toDto(Produit entity) {
        if(entity == null) {
            return null;
        }else{
            ProduitGetDto dto=new ProduitGetDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getDescription(),
                    entity.getPrix(),
                    entity.getQuantite(),
                    entity.getImage(),
                    entity.getCategorieId(),
                    entity.getFournisseurId(),
                    entity.getImagesId(),
                    entity.getStockId(),
                    entity.getEtatProduit()
            );
            return dto;
        }
    }
    public Produit toEntityPost(ProduitPostDto dto) {
        if(dto == null) {
            return null;
        }else{
            Produit entity=new Produit();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            entity.setDescription(dto.description());
            entity.setPrix(dto.prix());
            entity.setQuantite(dto.quantite());
            entity.setImage(dto.image());
            entity.setEtatProduit(dto.etatProduit());
            return entity;
        }
    }


    public ProduitPostDto toDtoPost(Produit entity) {
        if(entity == null) {
            return null;
        }else{
            ProduitPostDto dto=new ProduitPostDto(
                    entity.getId(),
                    entity.getNom(),
                    entity.getDescription(),
                    entity.getPrix(),
                    entity.getQuantite(),
                    entity.getImage(),
                    entity.getEtatProduit());
            return dto;
        }
    }


}

