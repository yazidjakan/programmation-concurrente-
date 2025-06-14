# 📦 Microservice Système de gestion de commandes en ligne - Spring WebFlux

## 🧠 Description du Projet

Ce projet consiste en un microservice **réactif** de gestion des commandes dans une architecture e-commerce distribuée, utilisant **Spring WebFlux** pour des performances non bloquantes.  

---

## ⚙️ Technologies principales

- **Spring Boot WebFlux** – programmation réactive et non bloquante
- **MongoDB** – base de données NoSQL orientée document
- **Project Reactor** – support natif de Flux et Mono
- **Micrometer + Prometheus/Grafana** – métriques de performance
- **JMeter** – tests de charge
- **Lombok / MapStruct** – simplification du code Java

---

## 🚀 Fonctionnalités Clés

- Création, consultation, mise à jour et suppression de commandes
- Envoi automatique de messages Kafka à d'autres services à la création
- Gestion d'état de commande : `SOUMISE`, `EN_COURS`, `ARRIVEE`
- Mesure automatique du temps d’exécution de chaque endpoint via **Micrometer**
- Endpoints REST sécurisés et exposés avec `@RestController`

---


## 📊 Résultats des Performances

- Tous les endpoints sont chronométrés via `Micrometer`.
- Des tests de charge ont été réalisés avec **Apache JMeter**.
- Les résultats sont enregistrés sous forme de métriques (durée moyenne d’exécution, débit…).

📎 Voir le rapport PDF des performances : [`resultats_micrometer_courbes.pdf`](./resultats_micrometer_courbes.pdf)

---

## 📽️ Démo Vidéo

Une vidéo est incluse dans ce dépôt montrant un test réel via JMeter.

---

## ✅ Lancement Local

1. Lancer `commande-service` avec `mvn spring-boot:run`
2. Utiliser Swagger ou Postman pour tester les endpoints :
   - `POST /api/v1/commandes/`
   - `GET /api/v1/commandes/id/{id}`

---

## 🧪 Exemple d'appel POST

```json
{
  "userId": "123456",
  "etatCommande": "SOUMISE"
}
```

---

## 📁 Structure du Projet

```
├── src/
│   ├── controller/
│   ├── service/
│   ├── dto/
│   ├── model/
│   └── config/
├── test/
├── README.md
```

---
