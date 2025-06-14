package com.ecomfsrwebflux.client_service.entity;

import com.ecomfsrwebflux.client_service.enums.EtatCommande;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "commandes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Commande {
    @Id
    private String id;

    // Référence vers un utilisateur (relation manuelle MongoDB)
    @DBRef(lazy = true)
    private User user;

    private EtatCommande etatCommande;
}
