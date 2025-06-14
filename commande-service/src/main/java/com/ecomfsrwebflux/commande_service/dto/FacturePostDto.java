package com.ecomfsrwebflux.commande_service.dto;

import java.time.LocalDateTime;

public record FacturePostDto(String id,
                             Double montantTotal,
                             LocalDateTime dateCreation,
                             String statut) {
}
