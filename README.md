# Gestion Surveillance

## Backend - Description et Installation

### Description

Le backend de "Gestion Surveillance" est une application Spring Boot qui fournit les API RESTful nécessaires pour gérer les données de l'application. Il gère la persistance des données via une base de données relationnelle.

### Prérequis

- Java JDK 17 ou supérieur

- Maven 3.6 ou supérieur

- MySQL 8.0 ou supérieur

### Installation

1. Clonez le repository du backend :

```bash

git clone https://github.com/votre-utilisateur/gestion-surveillance-backend.git

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

Le serveur sera disponible à l'adresse : http://localhost:8080

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


