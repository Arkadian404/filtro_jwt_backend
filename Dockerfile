FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

#COPY target/security-0.0.1-SNAPSHOT.jar /app/filtro.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","-jar","/app/filtro.jar"]

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]