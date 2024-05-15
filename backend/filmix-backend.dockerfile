#Budiling Stage
FROM maven:3.9.6-sapmachine-17 AS build-stage
#Using Ali Maven Repository
COPY maven_ali_settings.xml /usr/share/maven/conf/settings.xml

ADD pom.xml /pom.xml
ADD src /src/
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build-stage /target/filmix-backend-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 19190
CMD ["java", "-jar", "/app.jar","--spring.profiles.active = docker"]