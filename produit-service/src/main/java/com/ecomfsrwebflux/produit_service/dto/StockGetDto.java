package com.ecomfsrwebflux.produit_service.dto;

import com.ecomfsrwebflux.produit_service.enums.StatutStock;

import java.util.List;

public record StockGetDto(
        String id,
        int quantite,
        StatutStock statut,
        List<String> produitIds
) {}

