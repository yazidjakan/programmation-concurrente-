package com.ecomfsrwebflux.commande_service.dto;

import com.ecomfsrwebflux.commande_service.enums.EtatCommande;

public record CommandeGetDto(String id,
                             String userId,
                             EtatCommande etatCommande
) {
}
