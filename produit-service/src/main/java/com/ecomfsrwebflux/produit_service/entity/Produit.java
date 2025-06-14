package com.ecomfsrwebflux.produit_service.entity;

import com.ecomfsrwebflux.produit_service.enums.EtatProduit;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "produits")
public class Produit {

    @Id
    private String id;

    private String nom;
    private String description;
    private Double prix;
    private int quantite;
    private String image;

    private String categorieId;

    private String fournisseurId;

    private List<String> imagesId;

    private String stockId;

    private EtatProduit etatProduit;
}

