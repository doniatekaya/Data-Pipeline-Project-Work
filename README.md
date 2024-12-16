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
### **Lancer Kafka**

### 1. Démarrer Zookeeper
```
C:\kafka> .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

### 2. Démarrer le serveur Kafka
```
C:\kafka> .\bin\windows\kafka-server-start.bat .\config\server.properties
```

### 3. Créer le topic Kafka
```
kafka-topics --create --topic weather_data --bootstrap-server localhost:9092
```
---
### **Lancer flink**
```
./bin/stop-cluster.sh  
./bin/start-cluster.sh
```

### 2.Accédez al'interface 
```
http://localhost:8081
```
---

### **Exécution du Projet**

### 1.compilez le projet :
```
mvn clean install

```

### 2.Observez les logs pour lire les messages publiés dans `weather_data` :
```
kafka-console-consumer --bootstrap-server localhost:9092 --topic weather_data --from-beginning
```
<img width="799" alt="kafka topic consumer" src="https://github.com/user-attachments/assets/c9fbdb22-64f0-424a-a95c-144124cb459c" />


### 3.Exécutez le Producteur class :

```
mvn exec:java -"Dexec.mainClass=org.example.WeatherDataProducer"
```

<img width="767" alt="sent data " src="https://github.com/user-attachments/assets/00fa17c9-d8b5-4a42-8afc-a71722365d53" />


### 4.Exécutez le Consommateur class :

```
mvn exec:java -"Dexec.mainClass=org.example.WeatherDataConsumer"

```
<img width="786" alt="consumed data " src="https://github.com/user-attachments/assets/988b1fbd-5787-4039-ab98-333e6cdd9096" />



### 5. Exécutez le Job Flink  
Lancez la classe `FlinkSlidingWindow` pour consommer les données Kafka, appliquer une fenêtre glissante, et calculer une transformation.

```bash
mvn exec:java -Dexec.mainClass="org.example.FlinkSlidingWindow"

```

------

## **Exemple de Résultat**

Après traitement des flux de données avec une fenêtre glissante dans Apache Flink, voici un exemple de sortie agrégée des températures moyennes par ville :

- **City: Paris**  
  Avg Temperature: 12.34°C

- **City: Tokyo**  
  Avg Temperature: 15.67°C

- **City: Rome**  
  Avg Temperature: 9.12°C
----
## **Visualization

première Figure :

Variation de l'Humidité pour Différentes Villes :
Cette figure montre l'évolution de l'humidité (%) pour cinq villes différentes (Rome, Madrid, Paris, Istanbul, Tokyo) 

![output (1)](https://github.com/user-attachments/assets/6f66dd52-55d7-454f-addd-bb3850bbe47d)



Deuxième Figure (4 sous-graphes) :

![output](https://github.com/user-attachments/assets/58e2fd2e-5d82-4e6d-92b9-57ede0643e4e)


es graphiques montrent les principales variations climatiques à Rome sur une année. La température présente des variations saisonnières avec des pics en été et des creux en hiver. L’humidité reflète les fluctuations annuelles, mettant en évidence les périodes sèches et humides. La vitesse du vent permet d’observer les périodes venteuses et plus calmes. Enfin, la pression atmosphérique met en évidence les phases de haute et basse pression.


  





