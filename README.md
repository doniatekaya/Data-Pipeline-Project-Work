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
