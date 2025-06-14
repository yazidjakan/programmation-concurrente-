package com.ecomfsrwebflux.commande_service.entity;

import com.ecomfsrwebflux.commande_service.enums.EtatCommande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "commandes")
public class Commande {

    @Id
    private String id;

    private String userId;

    private EtatCommande etatCommande;
}
