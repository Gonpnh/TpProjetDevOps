# Projet JAVA ALTN72 - Yassine Gonçalo Terence

## CI, Couverture et Qualite

Le workflow GitHub Actions [CI](.github/workflows/ci.yml) execute :

1. Build + tests (`./mvnw -B clean verify`)
2. Generation de la couverture JaCoCo (`target/site/jacoco/jacoco.xml`)
3. Publication du rapport JUnit dans les checks GitHub
4. Upload de la couverture vers Codecov
5. Analyse SonarCloud (si les secrets sont definis)

## Secrets GitHub requis

Configurer ces secrets dans le repository pour activer toutes les integrations :

- `CODECOV_TOKEN` : token Codecov
- `SONAR_TOKEN` : token SonarCloud
- `SONAR_PROJECT_KEY` : cle du projet SonarCloud
- `SONAR_ORGANIZATION` : organisation SonarCloud

L'etape SonarCloud est ignoree automatiquement si `SONAR_TOKEN` n'est pas configure (utile pour les forks).