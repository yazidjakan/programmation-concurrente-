package com.ecomfsrwebflux.produit_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "categories")
public class Categorie {

    @Id
    private String id;

    private String nom;
    private String description;

    private List<String> produitIds;

}

