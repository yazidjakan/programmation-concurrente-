package com.ecomfsrwebflux.produit_service.dto;

public record FournisseurPostDto(
        String id,
        String nom,
        String adresse,
        String telephone,
        String email
) {}

