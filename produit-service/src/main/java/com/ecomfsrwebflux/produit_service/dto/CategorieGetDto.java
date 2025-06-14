package com.ecomfsrwebflux.produit_service.dto;

import java.util.List;

public record CategorieGetDto(
        String id,
        String nom,
        String description,
        List<String> produitIds
) {}

