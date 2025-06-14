package com.ecomfsrwebflux.produit_service.dto;

import com.ecomfsrwebflux.produit_service.enums.StatutStock;

public record StockPostDto(
        String id,
        int quantite,
        StatutStock statut
) {}

