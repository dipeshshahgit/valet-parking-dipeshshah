# syntax=docker/dockerfile:experimental
########build stage########
FROM maven:3.6-jdk-8 AS build
# WORKDIR /app  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM gcr.io/distroless/java  
COPY --from=build /usr/src/app/target/ValetParkingApplication-1.0-SNAPSHOT.jar /usr/app/ValetParkingApplication-1.0-SNAPSHOT.jar
COPY --from=build /usr/src/app/src/main/resources/input-data.txt /usr/app/input-data.txt
ENTRYPOINT ["java","-jar","/usr/app/ValetParkingApplication-1.0-SNAPSHOT.jar", "/usr/app/input-data.txt"]