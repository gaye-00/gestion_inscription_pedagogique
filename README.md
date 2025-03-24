# Système de Gestion des Inscriptions Pédagogiques

## Description

Ce projet est un système de gestion des inscriptions pédagogiques pour l'Université Assane Seck de Ziguinchor (UASZ). Il permet aux étudiants de s'inscrire aux UEs (Unités d'Enseignement) et aux responsables pédagogiques de gérer les formations, les groupes et les inscriptions.

## Fonctionnalités

- Gestion des étudiants
- Gestion des enseignants
- Gestion des formations et UEs
- Gestion des groupes de TD
- Système d'authentification pour étudiants et responsables pédagogiques
- Notifications par email

## Structure du projet

Le projet est structuré selon le modèle MVC (Modèle-Vue-Contrôleur) :

- **Model** : Classes représentant les entités métier (Etudiant, Enseignant, Formation, UE, etc.)
- **DAO** : Classes d'accès aux données pour chaque entité
- **GUI** : Interfaces utilisateur pour les étudiants et les responsables pédagogiques
- **Services** : Logique métier et services (email, etc.)

## Prérequis

- Java 11 ou supérieur
- Maven
- Base de données MySQL

## Installation

1. Cloner le dépôt

```bash
git clone <url-du-depot>
cd gaye-00-gestion_inscription_pedagogique
```

2. Configuration de la base de données

   - Créer une base de données MySQL
   - Exécuter le script SQL dans `/src/main/resources/gestion_inscription_pedagogique_2025-03-18_200552.sql`
   - Configurer les paramètres de connexion dans `/src/main/resources/META-INF/persistence.xml`

3. Compiler et exécuter le projet

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="sn.uasz.m1.projet.App"
```

## Utilisation

### Interface étudiant

- S'authentifier avec les identifiants étudiants
- Consulter les UEs disponibles
- S'inscrire aux UEs
- Visualiser son emploi du temps et ses inscriptions

### Interface responsable pédagogique

- Gérer les étudiants (ajout, modification, suppression)
- Gérer les formations et UEs
- Créer et assigner des groupes de TD
- Valider les inscriptions pédagogiques

## Diagrammes de conception

Les diagrammes de conception sont disponibles dans le dossier `/conception`.

## Importation de données

Un fichier exemple pour l'importation des étudiants est disponible : `Liste_Etudiants_Groupe-TD-1.csv`.

## Licence

Ce projet est développé dans le cadre d'un module de Master 1 à l'UASZ.

## Auteurs

### Seydina Mouhamadou Al Hamine NDIAYE

### Abdoulaye GAYE
