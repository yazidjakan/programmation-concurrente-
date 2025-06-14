package com.ecomfsrwebflux.commande_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "factures")
public class Facture {

    @Id
    private String id;

    private String reference;

    private Double montantTotal;

    private LocalDateTime dateCreation;

    private String commandeId;

    private String statut;
}

