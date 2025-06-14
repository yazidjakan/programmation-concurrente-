package com.ecomfsrwebflux.produit_service.entity;

import com.ecomfsrwebflux.produit_service.enums.StatutStock;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "stocks")
public class Stock {

    @Id
    private String id;

    private int quantite;

    private StatutStock statut;

    private List<String> produitIds;
}

