# Template Spring Boot + React avec Sécurité Complète

Un template fullstack moderne avec authentification JWT sécurisée, OAuth2 et architecture propre.

## 📋 Table des matières

- [Description](#-description)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Prérequis](#-prérequis)
- [Installation et Configuration](#-installation-et-configuration)
- [Structure du Projet](#-structure-du-projet)
- [Sécurité](#-sécurité)
- [API Documentation](#-api-documentation)
- [Tests](#-tests)
- [Déploiement](#-déploiement)
- [Auteur](#-auteur)
- [Licence](#-licence)

## 🎯 Description

Ce template fournit une base solide pour développer des applications web fullstack sécurisées. Il implémente les meilleures pratiques de sécurité recommandées par l'OWASP et offre une architecture modulaire et maintenable.

### Points forts

- **Sécurité de niveau bancaire** : Argon2 pour le hashage, JWT avec rotation des tokens, protection CSRF/XSS
- **Clean Architecture** : Séparation claire des responsabilités entre les couches
- **Prêt pour la production** : Rate limiting, audit logging, gestion des sessions
- **DX optimisée** : Hot reload, validation automatique, documentation Swagger

## ✨ Fonctionnalités

### Authentification & Autorisation
- ✅ Authentification JWT (Access Token + Refresh Token)
- ✅ OAuth2 (Google, Facebook)
- ✅ Vérification d'email obligatoire
- ✅ Réinitialisation de mot de passe
- ✅ Protection brute force avec verrouillage de compte
- ✅ Sessions multiples avec révocation

### Sécurité
- ✅ Hashage Argon2id (recommandation OWASP)
- ✅ Tokens en cookies HTTP-only
- ✅ Rate limiting par IP
- ✅ Headers de sécurité (CSP, HSTS, X-Frame-Options)
- ✅ Protection timing attack

### Gestion des utilisateurs
- ✅ Inscription avec validation email
- ✅ Gestion du profil
- ✅ Rôles et permissions (RBAC)
- ✅ Audit logging des actions sensibles

### Emails transactionnels
- ✅ Templates Thymeleaf
- ✅ Envoi asynchrone
- ✅ Email de vérification
- ✅ Email de réinitialisation de mot de passe
- ✅ Email de bienvenue

## 🏗️ Architecture

### Backend (Clean Architecture)

```
backend/src/main/java/com/company/backend/
├── config/           # Configuration Spring (Security, JWT, CORS, etc.)
├── controller/       # Contrôleurs REST (API endpoints)
├── domain/           # Entités JPA (User, Session, Token, etc.)
├── dto/              # Objets de transfert (Request/Response)
│   ├── request/      # DTOs des requêtes entrantes
│   └── response/     # DTOs des réponses sortantes
├── exception/        # Exceptions métier et handler global
├── mapper/           # Mappers MapStruct (Entity <-> DTO)
├── repository/       # Repositories Spring Data JPA
├── security/         # Filtres et handlers de sécurité
│   ├── jwt/          # Authentification JWT
│   └── oauth2/       # Authentification OAuth2
├── service/          # Interfaces des services métier
│   └── impl/         # Implémentations des services
└── util/             # Utilitaires (IP resolver, etc.)
```

### Frontend

```
frontend/src/
├── components/       # Composants React réutilisables
├── pages/            # Pages de l'application
├── hooks/            # Hooks personnalisés
├── lib/              # Utilitaires et configurations
└── types/            # Types TypeScript
```

### Diagramme de flux d'authentification

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           AUTHENTIFICATION                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  1. LOGIN                                                                │
│  ┌────────┐      ┌────────┐      ┌────────┐      ┌────────┐            │
│  │ Client │ ──▶  │  API   │ ──▶  │ Auth   │ ──▶  │  DB    │            │
│  │        │      │        │      │ Service│      │        │            │
│  │        │ ◀──  │        │ ◀──  │        │ ◀──  │        │            │
│  └────────┘      └────────┘      └────────┘      └────────┘            │
│      │                │                                                  │
│      │  Access Token (Cookie HTTP-only)                                 │
│      │  Refresh Token (Cookie HTTP-only)                                │
│      ▼                                                                   │
│                                                                          │
│  2. REQUÊTES AUTHENTIFIÉES                                              │
│  ┌────────┐      ┌────────┐      ┌────────┐                            │
│  │ Client │ ──▶  │  JWT   │ ──▶  │  API   │                            │
│  │        │      │ Filter │      │        │                            │
│  │        │ ◀──  │        │ ◀──  │        │                            │
│  └────────┘      └────────┘      └────────┘                            │
│                                                                          │
│  3. REFRESH TOKEN                                                        │
│  ┌────────┐      ┌────────┐      ┌────────┐                            │
│  │ Client │ ──▶  │  API   │ ──▶  │Session │                            │
│  │        │      │/refresh│      │  DB    │                            │
│  │        │ ◀──  │        │ ◀──  │        │                            │
│  └────────┘      └────────┘      └────────┘                            │
│      │                                                                   │
│      │  Nouveaux tokens (rotation)                                      │
│      ▼                                                                   │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

## 🛠️ Technologies

### Backend
| Technologie | Version | Description |
|-------------|---------|-------------|
| Java | 21 | Langage de programmation |
| Spring Boot | 3.4 | Framework applicatif |
| Spring Security | 6 | Sécurité et authentification |
| JJWT | 0.13 | Gestion des tokens JWT |
| MapStruct | 1.5 | Mapping objets |
| PostgreSQL | 15+ | Base de données |
| Argon2 | - | Hashage des mots de passe |
| Bucket4j | - | Rate limiting |
| Thymeleaf | - | Templates emails |

### Frontend
| Technologie | Version | Description |
|-------------|---------|-------------|
| React | 19 | Bibliothèque UI |
| TypeScript | 5.9 | Typage statique |
| Vite | 7 | Build tool |
| Tailwind CSS | 4 | Framework CSS |
| Shadcn/ui | - | Composants UI |
| React Hook Form | 7 | Gestion des formulaires |
| Zod | 4 | Validation des données |
| Axios | 1.13 | Client HTTP |

## 📦 Prérequis

- **Java** 21 ou supérieur
- **Node.js** 20 ou supérieur
- **PostgreSQL** 15 ou supérieur
- **Maven** 3.9 ou supérieur

## 🚀 Installation et Configuration

### 1. Cloner le repository

```bash
git clone https://github.com/Benseddik-Fethi/template-spring-react-security.git
cd template-spring-react-security
```

### 2. Configuration de la base de données

Créer une base de données PostgreSQL :

```sql
CREATE DATABASE template_db;
CREATE USER template_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE template_db TO template_user;
```

### 3. Variables d'environnement

#### Backend (`backend/application.yml` ou variables d'environnement)

```yaml
# Base de données
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/template_db
    username: template_user
    password: ${DB_PASSWORD}

# JWT - CRITIQUE : Générer un secret de 64+ caractères
jwt:
  secret: ${JWT_SECRET}  # openssl rand -base64 64
  access-token:
    expiration: 15m
  refresh-token:
    expiration: 7d
  issuer: template-api
  audience: template-app

# OAuth2 (optionnel)
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}

# Email (optionnel)
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}

# Application
app:
  frontend-url: http://localhost:5173
  name: Template App
  security:
    cors:
      allowed-origins:
        - http://localhost:5173
    rate-limit:
      enabled: true
      requests-per-minute: 60
      auth-requests-per-minute: 10
    brute-force:
      max-attempts: 5
      lock-duration: 15m
```

#### Frontend (`.env`)

```env
VITE_API_URL=http://localhost:8080/api/v1
```

### 4. Lancement

#### Backend

```bash
cd backend
mvn spring-boot:run
```

Le backend démarre sur http://localhost:8080

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

Le frontend démarre sur http://localhost:5173

### 5. Comptes de test (profil non-prod)

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| admin@template.com | Password123! | ADMIN |
| user@template.com | Password123! | USER |

## 📁 Structure du Projet

```
template-spring-react-security/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/company/backend/
│   │   │   │   ├── BackendApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── JwtProperties.java
│   │   │   │   │   ├── SecurityProperties.java
│   │   │   │   │   └── ...
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   └── UserController.java
│   │   │   │   ├── domain/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Session.java
│   │   │   │   │   ├── AuditLog.java
│   │   │   │   │   └── ...
│   │   │   │   ├── dto/
│   │   │   │   ├── exception/
│   │   │   │   ├── mapper/
│   │   │   │   ├── repository/
│   │   │   │   ├── security/
│   │   │   │   ├── service/
│   │   │   │   └── util/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── templates/email/
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
└── README.md
```

## 🔐 Sécurité

### Authentification JWT

#### Access Token
- **Durée** : 15 minutes (configurable)
- **Stockage** : Cookie HTTP-only
- **Contenu** : ID utilisateur, email, rôle

#### Refresh Token
- **Durée** : 7 jours (configurable)
- **Stockage** : Cookie HTTP-only + hash SHA-256 en base
- **Rotation** : Nouveau token à chaque refresh

### Hashage des mots de passe

Utilisation d'**Argon2id** avec les paramètres OWASP 2024 :

| Paramètre | Valeur | Description |
|-----------|--------|-------------|
| Salt | 16 bytes | Sel aléatoire |
| Hash | 32 bytes | Longueur du hash |
| Parallelism | 4 | Threads CPU |
| Memory | 64 MB | Mémoire utilisée |
| Iterations | 4 | Nombre de passes |

### Headers de sécurité

```
Content-Security-Policy: default-src 'self'; script-src 'self'; ...
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
Referrer-Policy: strict-origin-when-cross-origin
Permissions-Policy: geolocation=(), microphone=(), camera=()
```

### Rate Limiting

| Endpoint | Limite | Fenêtre |
|----------|--------|---------|
| Général | 60 requêtes | 1 minute |
| Authentification | 10 requêtes | 1 minute |

### Protection Brute Force

- **Tentatives max** : 5
- **Durée verrouillage** : 15 minutes
- **Logging** : Toutes les tentatives sont enregistrées

### Audit Logging

Actions enregistrées :
- Connexion réussie/échouée
- Déconnexion
- Verrouillage de compte
- Changement de mot de passe
- Connexion OAuth2
- Vérification d'email

## 📡 API Documentation

### Endpoints d'authentification

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/v1/auth/register` | Inscription | Non |
| POST | `/api/v1/auth/login` | Connexion | Non |
| POST | `/api/v1/auth/refresh` | Rafraîchir les tokens | Non |
| POST | `/api/v1/auth/logout` | Déconnexion | Oui |
| POST | `/api/v1/auth/logout-all` | Déconnexion totale | Oui |
| GET | `/api/v1/auth/me` | Utilisateur courant | Oui |
| POST | `/api/v1/auth/oauth/exchange` | Échanger code OAuth2 | Non |

### Endpoints utilisateurs

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/v1/users/verify-email` | Vérifier email | Non |
| POST | `/api/v1/users/resend-verification` | Renvoyer vérification | Non |
| POST | `/api/v1/users/forgot-password` | Demander reset | Non |
| GET | `/api/v1/users/reset-password/validate` | Valider token reset | Non |
| POST | `/api/v1/users/reset-password` | Réinitialiser mdp | Non |
| POST | `/api/v1/users/change-password` | Changer mdp | Oui |
| GET | `/api/v1/users/profile` | Voir profil | Oui |

### Exemples de requêtes

#### Inscription
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### Connexion
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -c cookies.txt \
  -d '{
    "email": "user@example.com",
    "password": "SecurePass123!"
  }'
```

#### Requête authentifiée
```bash
curl -X GET http://localhost:8080/api/v1/auth/me \
  -b cookies.txt
```

## 🧪 Tests

### Backend

```bash
cd backend

# Tests unitaires et d'intégration
mvn test

# Avec rapport de couverture
mvn test jacoco:report
```

### Frontend

```bash
cd frontend

# Linting
npm run lint

# Build
npm run build
```

## 🚢 Déploiement

### Variables d'environnement de production

```bash
# CRITIQUE - Sécurité
JWT_SECRET=<secret-64-caractères-minimum>
DB_PASSWORD=<mot-de-passe-fort>

# Base de données
DATABASE_URL=jdbc:postgresql://host:5432/db

# OAuth2 (si utilisé)
GOOGLE_CLIENT_ID=<client-id>
GOOGLE_CLIENT_SECRET=<client-secret>

# Email (si utilisé)
MAIL_USERNAME=<email>
MAIL_PASSWORD=<app-password>

# Application
APP_FRONTEND_URL=https://votre-domaine.com
SPRING_PROFILES_ACTIVE=prod
```

### Docker (exemple)

```dockerfile
# Backend
FROM eclipse-temurin:21-jre
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Recommandations production

1. **HTTPS obligatoire** : Configurer SSL/TLS
2. **Cookies sécurisés** : Activer `Secure` et `SameSite=Strict`
3. **Rate limiting distribué** : Remplacer Caffeine par Redis
4. **Monitoring** : Activer Actuator et Prometheus
5. **Logs centralisés** : ELK Stack ou équivalent
6. **Backup BDD** : Configurer des sauvegardes automatiques

## 👤 Auteur

**Fethi Benseddik**

- GitHub: [@Benseddik-Fethi](https://github.com/Benseddik-Fethi)

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

⭐ Si ce template vous a été utile, n'hésitez pas à lui donner une étoile sur GitHub !
