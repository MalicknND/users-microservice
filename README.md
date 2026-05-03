# Users Microservice

## Description

Ce projet est un microservice Spring Boot dédié à la gestion des utilisateurs et à l'authentification via JWT (JSON Web Tokens). Il permet de gérer les utilisateurs, les rôles, et de générer des tokens JWT pour l'authentification sécurisée dans une architecture de microservices.

Le microservice fournit des fonctionnalités de base pour :
- Création et gestion des utilisateurs
- Gestion des rôles
- Authentification par login/mot de passe
- Génération de tokens JWT
- Autorisation basée sur les rôles

## Fonctionnalités

- **Gestion des utilisateurs** : Création, recherche par nom d'utilisateur
- **Gestion des rôles** : Ajout de rôles et association aux utilisateurs
- **Authentification JWT** : Génération de tokens sécurisés pour l'authentification stateless
- **Sécurité Spring Security** : Configuration complète avec filtres JWT personnalisés
- **Base de données MySQL** : Persistance des données utilisateurs et rôles

## Technologies utilisées

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA** : Pour l'accès aux données
- **Spring Security** : Pour la sécurité et l'authentification
- **JWT (Auth0)** : Pour les tokens d'authentification
- **MySQL** : Base de données
- **Lombok** : Pour réduire le code boilerplate
- **Maven** : Gestion des dépendances

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java 17** ou supérieur
- **Maven 3.6+**
- **MySQL 8.0+**
- Un IDE (IntelliJ IDEA, Eclipse, VS Code) avec support Spring Boot

## Installation

1. **Cloner le repository** :
   ```bash
   git clone <url-du-repository>
   cd users-microservice
   ```

2. **Configurer la base de données** :
   - Créer une base de données MySQL nommée `mydb2`
   - Modifier les paramètres de connexion dans `src/main/resources/application.properties` si nécessaire

3. **Compiler le projet** :
   ```bash
   ./mvnw clean compile
   ```

4. **Lancer l'application** :
   ```bash
   ./mvnw spring-boot:run
   ```

L'application sera accessible sur `http://localhost:8081/users`

## Configuration

Le fichier `application.properties` contient la configuration principale :

```properties
spring.application.name=users-microservice
spring.datasource.url=jdbc:mysql://localhost:3307/mydb2?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
server.servlet.context-path=/users
server.port=8081
spring.main.allow-circular-references=true
```

### Variables d'environnement

Vous pouvez personnaliser :
- `spring.datasource.url` : URL de connexion MySQL
- `spring.datasource.username` : Nom d'utilisateur MySQL
- `spring.datasource.password` : Mot de passe MySQL
- `server.port` : Port du serveur (défaut: 8081)
- `server.servlet.context-path` : Contexte de l'application (défaut: /users)

### Sécurité JWT

Les paramètres de sécurité sont définis dans `SecParams.java` :
- **SECRET** : Clé secrète pour signer les tokens
- **EXP_TIME** : Durée d'expiration (10 jours)
- **PREFIX** : Préfixe du token ("Bearer ")

⚠️ **Important** : En production, changez la clé secrète et utilisez des variables d'environnement pour la sécurité.

## Utilisation

### Authentification

Pour vous authentifier et obtenir un token JWT :

**Endpoint** : `POST /users/login`

**Corps de la requête** :
```json
{
  "username": "admin",
  "password": "123"
}
```

**Réponse** : Header `Authorization` contenant le token JWT

**Exemple avec curl** :
```bash
curl -X POST http://localhost:8081/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123"}'
```

### Utilisation du token

Pour accéder aux endpoints protégés, incluez le token dans le header Authorization :

```bash
curl -H "Authorization: Bearer <votre-token-jwt>" \
  http://localhost:8081/users/protected-endpoint
```

## API Endpoints

### Authentification
- `POST /login` - Authentification et génération de token JWT

### Gestion des utilisateurs (à implémenter)
- `POST /users` - Créer un utilisateur
- `GET /users/{username}` - Récupérer un utilisateur
- `PUT /users/{username}/roles` - Ajouter un rôle à un utilisateur

## Modèle de données

### User
```java
{
  user_id: Long,
  username: String (unique),
  password: String (hashé),
  enabled: Boolean,
  roles: List<Role>
}
```

### Role
```java
{
  role_id: Long,
  role: String
}
```

## Sécurité

- **Authentification stateless** : Utilisation de JWT au lieu de sessions
- **Chiffrement des mots de passe** : BCrypt
- **Protection CSRF désactivée** : Pour les API REST
- **Filtrage JWT** : Vérification automatique des tokens sur chaque requête
- **Autorisation basée sur les rôles** : Spring Security avec GrantedAuthority

## Base de données

Le microservice utilise MySQL avec JPA/Hibernate :
- **DDL auto** : `update` (mise à jour automatique du schéma)
- **Tables** : `user`, `role`, `user_role` (table de jointure)
- **Logs SQL** : Activés pour le développement

## Tests

Pour exécuter les tests :
```bash
./mvnw test
```

## Développement

### Structure du projet
```
src/main/java/org/msn/usersmicroservice/
├── entities/          # Entités JPA (User, Role)
├── repos/            # Repositories Spring Data
├── service/          # Services métier
├── security/         # Configuration sécurité et filtres JWT
└── UsersMicroserviceApplication.java
```

### Ajout de contrôleurs REST

Pour ajouter des endpoints REST, créez des classes annotées `@RestController` dans un package `controllers/`.

Exemple :
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    // endpoints pour la gestion des utilisateurs
}
```

## Déploiement

### JAR exécutable
```bash
./mvnw clean package
java -jar target/users-microservice-0.0.1-SNAPSHOT.jar
```

### Docker (optionnel)
Créer un Dockerfile et utiliser Docker Compose pour la base de données.

## Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit les changements (`git commit -am 'Ajout nouvelle fonctionnalité'`)
4. Push la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créer une Pull Request

## Licence

Ce projet est sous licence [MIT](LICENSE).

## Support

Pour toute question ou problème, veuillez créer une issue dans le repository GitHub.

---

**Note** : Ce microservice est conçu pour être utilisé dans une architecture de microservices. Assurez-vous que les autres services vérifient correctement les tokens JWT émis par ce service.</content>
<parameter name="filePath">/Volumes/SSD_DEV/kuikops/spring/users-microservice/README.md
