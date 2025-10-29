# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# récupère le jar généré (nom quelconque *-SNAPSHOT.jar)
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
# Render fournit $PORT : on le passe à Spring
CMD ["sh","-c","java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
