# sport-discovery-api

API backend en Kotlin / Spring Boot 3 : un catalogue d'expériences sportives et un moteur de recommandations personnalisées.

## Endpoints

| Méthode | Route | Rôle |
|---|---|---|
| GET | `/api/experiences?sport=&city=` | Lister / filtrer le catalogue |
| GET | `/api/experiences/{id}` | Détail (404 en ProblemDetail si absent) |
| POST | `/api/experiences` | Créer (validation Bean Validation) |
| DELETE | `/api/experiences/{id}` | Supprimer |
| POST | `/api/recommendations?limit=5` | Recommandations à partir d'un profil |
| GET | `/actuator/health`, `/actuator/prometheus` | Observabilité |

Chaque recommandation renvoie un score et les raisons qui l'expliquent.

## Choix

- Scoring par contenu (sport aimé, tags communs, proximité) : simple, testable et explicable, une base avant un modèle ML.
- Persistance Spring Data JPA et migrations Flyway (`db/migration`). H2 en mode PostgreSQL par défaut, PostgreSQL avec le profil `postgres` (`docker compose up -d` puis `SPRING_PROFILES_ACTIVE=postgres ./gradlew bootRun`).
- Le scoring est une liste de `ScoringRule` (interface fonctionnelle) : ajouter un critère, c'est ajouter une règle, chacune testée seule (`ScoringRuleTest`).

## Lancer

```bash
./gradlew bootRun   # API sur :8080
./gradlew test
docker build -t sport-discovery-api .
```

La CI GitHub Actions exécute `./gradlew build` à chaque push.

Le déploiement Dokploy utilise PostgreSQL 18.7 et monte le volume `pgdata18` dans `/var/lib/postgresql`. Si le déploiement utilise encore PostgreSQL 16 et le volume `pgdata`, suspendre les écritures avant la sauvegarde finale. Restaurer cette sauvegarde dans `pgdata18` avec PostgreSQL 18, puis valider les données et le démarrage de l'API avant de rouvrir les écritures. Garder l'ancien volume pour le retour arrière avant la réouverture.

Les identifiants `discovery/discovery` (compose et profil `postgres`) sont des valeurs de développement, pas de production. Les données de démo sont dans la migration `V2__seed.sql`.
