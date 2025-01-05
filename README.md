# Gestion Surveillance

## Backend - Description et Installation

### Description

Le backend de "Gestion Surveillance" est une application Spring Boot qui fournit les API RESTful nécessaires pour gérer les données de l'application. Il gère la persistance des données via une base de données relationnelle.

### Prérequis

- Java JDK 11 ou supérieur

- Maven 3.6 ou supérieur

- MySQL 8.0 ou supérieur

### Installation

1. Clonez le repository du backend :

```bash

git clone https://github.com/votre-utilisateur/gestion-surveillance-backend.git

```

2. Accédez au répertoire du projet :

```bash

cd gestion-surveillance-backend

```

3. Configurez la base de données dans src/main/resources/application.properties

4. Compilez et lancez l'application :

```bash

mvn spring-boot:run

```

Le serveur sera disponible à l'adresse : http://localhost:8080

### API Endpoints

* /api/auth - Authentification

* /api/examens - Gestion des examens

* /api/modules - Gestion des modules

* /api/departements - Gestion des départements

* /api/locaux - Gestion des locaux

* /api/enseignants - Gestion des enseignants

### Technologies utilisées

* Spring Boot

* Spring Security

* Spring Data JPA

* MySQL

* Maven

## Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Forkez le projet

2. Créez une branche pour votre fonctionnalité

3. Committez vos changements

4. Poussez vers la branche

5. Ouvrez une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.
