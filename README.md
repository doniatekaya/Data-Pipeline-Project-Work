# Data-Pipeline-Project-Work

Le projet vise à collecter, traiter et analyser des **flux de données météorologiques** en temps réel en utilisant **Apache Kafka** et **Apache Flink**. Il met en place une infrastructure permettant de :

1. **Récupérer les données météorologiques** pour plusieurs villes à partir de l'API OpenWeatherMap.
2. **Publier ces données dans un topic Kafka** sous forme de flux en temps réel.
3. **Consommer ces flux de données** avec Apache Flink pour effectuer des analyses en continu, telles que :
   - Calculs de moyennes,
   - Agrégations par ville,
   - Visualisations de tendances météorologiques.

## **API utilisée**
- **Nom** : [OpenWeatherMap](https://api.openweathermap.org/data/2.5/weather)  
- **Données disponibles** : 
  - Température
  - Humidité
  - Pression
  - Vitesse du vent
  - Couverture nuageuse
- **Mode d'accès** : Requêtes HTTP avec une clé API pour des données en temps réel.

---
## **Dépendances Utilisées**

- **Apache Kafka** : Version 3.4.0  
  Utilisé pour gérer les flux de données en temps réel (publication et consommation).

- **Apache Flink** : Version 1.20.0  
  Utilisé pour consommer, transformer, et analyser les flux de données avec des fenêtres glissantes.

- **Java** : Version 1.8  
  Langage utilisé pour implémenter les producteurs, consommateurs, et transformations.

- **Maven** : Version 3.10.1  
  Utilisé pour la gestion des dépendances et la compilation du projet.

---
## **Configuration du Projet**

1. **Création d'un Projet Maven**
   - Lancez votre IDE (par exemple, IntelliJ IDEA ou Eclipse).
   - Sélectionnez l'option **Create Maven Project**.
   - Entrez les informations suivantes :
     - **groupId** : `org.example`
     - **artifactId** : `flink-project`
   - Validez pour générer la structure initiale du projet.

2. **Ajout des Dépendances**
   - Ouvrez le fichier `pom.xml` généré automatiquement.
  
---


