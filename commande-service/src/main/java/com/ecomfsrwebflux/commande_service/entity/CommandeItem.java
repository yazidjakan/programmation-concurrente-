package com.ecomfsrwebflux.commande_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "commande_items")
public class CommandeItem {

    @Id
    private String id;

    private String produitId;

    private Integer quantite;

    private String commandeId;

    private String userId;
}

