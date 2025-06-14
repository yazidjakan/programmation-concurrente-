package com.ecomfsrwebflux.commande_service.dto;

import java.time.LocalDateTime;

public record FactureGetDto(String id,
                            String reference,
                            Double montantTotal,
                            LocalDateTime dateCreation,
                            String commandeId,
                            String statut) {
}
