package com.ecomfsrwebflux.produit_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "images_produits")
public class ImageProduit {

    @Id
    private String id;

    private String url;

    private String produitId;
}

