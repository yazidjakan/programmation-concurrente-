package com.ecomfsrwebflux.produit_service.dto;

import com.ecomfsrwebflux.produit_service.enums.EtatProduit;

import java.util.List;

public record ProduitGetDto(
        String id,
        String nom,
        String description,
        Double prix,
        int quantite,
        String image,
        String categorieId,
        String fournisseurId,
        List<String> imageIds,
        String stockId,
        EtatProduit etatProduit
) {}

