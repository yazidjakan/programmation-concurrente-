package com.ecomfsrwebflux.commande_service.dto;

public record CommandeItemGetDto(String id,
                                 String produitId,
                                 Integer quantite,
                                 String commandeId) {
}
