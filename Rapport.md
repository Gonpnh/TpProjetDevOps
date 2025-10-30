# Rapport de projet

Ce document sert de trame pour remplir le rapport demandé (sections 3.1 à 3.4 et l'analyse SQL).
---

## 3.1 Identifiants de test

- Login (compte de test pour le prof) : asta
- Mot de passe : zMhFKW4KTjiLPO2W


---

## 3.2 Outillage

### 3.2.1 IDE utilisé
- IDE : Intellij IDEA Ultimate Edition

### 3.2.2 SGBD choisi
- SGBD : PostgreSQL (à la base MariaDB/MySQL, mais basculé sur PostgreSQL pour le déploiement sur Render.com)
- Version : 17

---

## 3.3 Instructions pour lancer et tester l'application (en local)

- Prérequis :
    - Java JDK version : 17
- Variables d'environnement (exemples à voir dans .env.example) :
    - SPRING_DATASOURCE_URL = jdbc:postgresql://localhost:5432/asta
    - SPRING_DATASOURCE_USERNAME = postgres
    - SPRING_DATASOURCE_PASSWORD = password

- URL utiles après démarrage :
    - Application web (Thymeleaf) : http://localhost:8080/
    - Swagger / OpenAPI UI : http://localhost:8080/swagger-ui/index.html (ou /swagger-ui.html)

---

## 3.4 Retour d'expérience et métadonnées

### a) Aspects à signaler au correcteur
- [À REMPLIR] : Indiquer ici l'aspect technique, fonctionnel ou pédagogique sur lequel tu souhaites attirer l'attention (ex. intégration Spring Security, architecture MVC, tests, gestion des migrations, etc.).

### b) Plus grande difficulté rencontrée
- Description de la difficulté : [À REMPLIR]
- Comment elle a été gérée / solutionnée / contournée : [À REMPLIR]

### c) Contribution de chaque membre de l'équipe
- Membre 1 (Yassine) :
- - Configuration initiale du projet Spring Boot avec la structure initiale et les dépendances nécessaires.
- - Création du script SQL de migration pour la création de la base de données et des tables.
- - Implémentation de Spring Security pour la gestion de l'authentification et des rôles utilisateurs ainsi que la sécurisation des endpoints et des URLs.
- - Configuration de SWAGGER / OpenAPI pour la documentation automatique de l'API REST.
- - Implémentation de certaines pages Thymeleaf comme le calendrier, le système d'évaluation...
- - Ajout de l'importation des données des étudiants via un fichier CSV.
- Membre 2 (Gonçalo) :
- - Intégration de Thymeleaf et développement des ControllerView pour relier les pages web à la logique du back-end.
- - Configuration de la navigation entre les différentes pages et gestion des liens et redirections dans l’interface utilisateur.
- - Déploiement complet de l’application sur Render.com, incluant la configuration du serveur, de la base de données et la mise en ligne du projet.
- - Contribution à la mise en forme du site avec le CSS pour améliorer l’ergonomie et l’esthétique des pages.
- Membre 3 (Terence) :
- - Création d'un workflow github pour vérifier que l'application ce build correctement à chaque commit
- - Réalisation des controller qui gèrent les API REST pour les entités principales (Apprenti, Evaluation, etc.).
- - Implémentation des services qui contiennent la logique métier pour les différentes entités.
- - Requête personnalisée avec SQL et JPQL pour filtrer le résultat

### d) Trois points retenus du cours / projet
1. Mise en œuvre du pattern MVC avec SpringBoot et Thymeleaf, avec la séparation de la logique métier, la gestion des interactions avec les controllers et l'affichage dynamique avec les view donc Thymeleaf.
2. L'utilisation de JPA et Hibernate pour la persistance qui nous a permis de manipuler la base de données (MySQL au début et PostgreSQL ensuite) sans écrire de SQL, en utilisant les entity, les repository et les requêtes dérivés.
3. La gestion des dépendances avec Maven qui a permis de centraliser la librairie du projet dans le fichier pom.xml et qui a permis de faciliter la maintenance et aussi le déploiement sur render.com.

### e) Fonctionnalités non implémentées et raisons
- Fonctionnalité 1 : [À REMPLIR] — raison : [À REMPLIR]
- Fonctionnalité 2 : [À REMPLIR] — raison : [À REMPLIR]

### f) Respect des principes SOLID
- Single Responsibility : Respecté — justification : Nous avons veillé à bien séparer les responsabilités entre les différentes couches : les controllers (comme ApprentiViewController ou EvaluationViewController) gèrent les requêtes et les vues, les services (comme ApprentiService) contiennent la logique métier, et les repositories (comme ApprentiRepository) accèdent aux données.
- Open/Closed : Respecté — justification : Nous avons structuré le projet de façon à pouvoir ajouter facilement de nouvelles pages ou fonctionnalités (comme le calendrier ou l’évaluation) sans modifier les classes existantes.
- Liskov Substitution : Respecté — justification : Nous avons utilisé des interfaces génériques Spring comme JpaRepository qui permettent à nos repositories (ApprentiRepository, EvaluationRepository, etc.) d’hériter d’un comportement standard et interchangeable.
- Interface Segregation : Respecté — justification : Chaque interface de repository est spécialisée pour une entité donnée : par exemple, ApprentiRepository gère uniquement les apprentis et EvaluationRepository uniquement les évaluations.
- Dependency Inversion : Respecté — justification : Nous avons mis en œuvre l’injection de dépendances pour découpler les classes :Les services comme ApprentiService l'utilisent pour recevoir automatiquement un ApprentiRepository. Les controllers comme ApprentiViewController injectent à leur tour ApprentiService sans jamais créer d’instance manuellement (new). Et enfin les annotations @Service et @Repository indiquent à Spring Boot de gérer lui-même les dépendances.

---

## B. Utilisation d'une requête SQL standard (unique)

### Analyse critique de l'utilisation d'une requête SQL native (une seule fois)

- Objectif de l'utilisation de SQL natif : 
- - Permettre de faire des requêtes personnalisées pour pouvoir avoir des résultats plus précis et ne pas avoir à trier les résultats dans le front
- Inconvénients / risques (portabilité, sécurité, perte d'abstraction, maintenance) :
    - ne s'adapte pas automatiquement aux changements de schéma de la base de données et au SGBD
- Alternatives possibles (JPQL, Spring Data derived queries, Criteria API) :
    - JPQL : Permet de faire des requêtes en utilisant les entités et leurs relations, ce qui est plus portable et maintenable.

---

## Annexes

- Fichiers importants à consulter :
    - `src/main/resources/application.properties`
    - `src/main/resources/db/migration` (Flyway SQLs)

---