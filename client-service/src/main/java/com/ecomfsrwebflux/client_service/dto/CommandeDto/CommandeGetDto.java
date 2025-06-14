package com.ecomfsrwebflux.client_service.dto.CommandeDto;


import com.ecomfsrwebflux.client_service.entity.User;
import com.ecomfsrwebflux.client_service.enums.EtatCommande;

public record CommandeGetDto(
        String id,
        User user,
        EtatCommande etatCommande
) {
}
