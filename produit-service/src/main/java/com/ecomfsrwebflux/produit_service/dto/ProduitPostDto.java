package com.ecomfsrwebflux.produit_service.dto;

import com.ecomfsrwebflux.produit_service.enums.EtatProduit;

public record ProduitPostDto(
        String id,
        String nom,
        String description,
        Double prix,
        int quantite,
        String image,
        EtatProduit etatProduit
) {}

