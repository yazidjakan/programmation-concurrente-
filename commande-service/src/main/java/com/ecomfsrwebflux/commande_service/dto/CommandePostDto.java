package com.ecomfsrwebflux.commande_service.dto;

import com.ecomfsrwebflux.commande_service.enums.EtatCommande;

public record CommandePostDto(String id,
                              EtatCommande etatCommande) {
}

