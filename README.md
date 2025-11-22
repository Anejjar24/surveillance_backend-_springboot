# Gestion Surveillance
## Description

Le backend de "Gestion Surveillance" est une application Spring Boot qui fournit les API RESTful nécessaires pour gérer les données de l'application. Il gère la persistance des données via une base de données relationnelle.

---
## Table des matières

   
1. [Prérequis](#prérequis)

2. [Installation](#installation)
 
3. [Configuration](#configuration)
 
4. [Technologies utilisées](#technologies-utilisées)
   
5. [Architecture du projet](#architecture-du-projet)
6. [API Endpoints](#api-endpoints)
 
---
 
### Prérequis

- Java JDK 17 ou supérieur

- Maven 3.6 ou supérieur

- MySQL 8.0 ou supérieur

### Installation

1. Clonez le repository du backend :

```bash

git clone https://github.com/Anejjar24/surveillance_backend-_springboot.git

```

2. Accédez au répertoire du projet :

```bash

cd surveillance_backend-_springboot-main

```

3. Configurez la base de données dans
src/main/resources/application.properties

5. Compilez et lancez l'application :

```bash

mvn spring-boot:run

```

Le serveur sera disponible à l'adresse : http://localhost:8082

### API Endpoints

* /api/auth - Authentification

* /sessions - Gestion des sessions

* /departments - Gestion des départements

* /locals - Gestion des locaux

* /enseignants - Gestion des enseignants 

* /options - Gestion des options

* /modules - Gestion des modules

* /exams - Gestion des examens 

### Technologies utilisées

* Spring Boot

* Spring Security

* Spring Data JPA

* MySQL

* Maven
### Architucture du projet
```
src/
├── main/
│   ├── java/
│   │   └── ma.ensaj.GestionSurveillance/
│   │       ├── config/           # ⚙️ Configurations Spring
│   │       ├── controllers/      # 🎮 Contrôleurs REST
│   │       ├── entities/         # 📦 Entités JPA
│   │       ├── repositories/     # 💾 Repositories Spring Data
│   │       ├── security/         # 🔒 Configuration sécurité
│   │       ├── services/         # 🔧 Services métier
│   │       └── GestionSurveillanceApplication.java  # 🚀 Point d'entrée de l'application
│   └── resources/
│       ├── static/              # 📁 Ressources statiques
│       ├── templates/           # 📑 Templates (si utilisés)
│       └── application.properties  # ⚙️ Configuration de l'application
```

### Configuration

La configuration de la base de données se fait dans le fichier `application.properties` situé dans `src/main/resources/`.

```properties
# Configuration de la base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/surveillance_jee
spring.datasource.username=votre_user
spring.datasource.password=votre_mot_de_passe
 ```



