package com.ecomfsrwebflux.client_service.dto.CommandeDto;


import com.ecomfsrwebflux.client_service.enums.EtatCommande;

public record CommandePostDto(
        String id,
        EtatCommande etatCommande
) {
}
