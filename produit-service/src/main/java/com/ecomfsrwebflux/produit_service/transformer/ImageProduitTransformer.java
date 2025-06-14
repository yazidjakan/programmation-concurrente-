package com.ecomfsrwebflux.produit_service.transformer;

import com.ecomfsrwebflux.produit_service.dto.ImageProduitGetDto;
import com.ecomfsrwebflux.produit_service.dto.ImageProduitPostDto;
import com.ecomfsrwebflux.produit_service.entity.ImageProduit;
import org.springframework.stereotype.Component;

@Component
public class ImageProduitTransformer extends AbstractTransformer<ImageProduit, ImageProduitGetDto> {
    @Override
    public ImageProduit toEntity(ImageProduitGetDto dto) {
        if (dto == null) {
            return null;
        } else {
            ImageProduit entity = new ImageProduit();
            entity.setId(dto.id());
            entity.setUrl(dto.url());
            entity.setProduitId(dto.produitId());
            return entity;
        }
    }

    @Override
    public ImageProduitGetDto toDto(ImageProduit entity) {
        if (entity == null) {
            return null;
        } else {
            ImageProduitGetDto dto = new ImageProduitGetDto(
                    entity.getId(),
                    entity.getUrl(),
                    entity.getProduitId()
            );
            return dto;
        }
    }

    public ImageProduit toEntityPost(ImageProduitPostDto dto) {
        if (dto == null) {
            return null;
        } else {
            ImageProduit entity = new ImageProduit();
            entity.setId(dto.id());
            entity.setUrl(dto.url()
            );
            return entity;
        }
    }

    public ImageProduitPostDto toDtoPost(ImageProduit entity) {
        if (entity == null) {
            return null;
        } else {
            ImageProduitPostDto dto = new ImageProduitPostDto(
                    entity.getId(),
                    entity.getUrl()
            );
            return dto;
        }
    }
}
