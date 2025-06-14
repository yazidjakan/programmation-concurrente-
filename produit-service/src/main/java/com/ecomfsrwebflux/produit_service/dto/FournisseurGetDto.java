package com.ecomfsrwebflux.produit_service.dto;

import java.util.List;

public record FournisseurGetDto(
        String id,
        String nom,
        String adresse,
        String telephone,
        String email,
        List<String> produitIds
) {}

