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
- Catalogue en mémoire derrière `CatalogService` : à remplacer par PostgreSQL sans toucher aux contrôleurs.

## Lancer

```bash
./gradlew bootRun   # API sur :8080
./gradlew test
docker build -t sport-discovery-api .
```

La CI GitHub Actions exécute `./gradlew build` à chaque push.
