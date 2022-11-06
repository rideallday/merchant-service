#
# Build stage
#
FROM maven:3.8.6-eclipse-temurin-11-focal AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
# This should be eclipse-temurin but it there is a bug with my old version of docker desktop
FROM openjdk:11 
COPY --from=build /home/app/target/merchant-0.0.1-SNAPSHOT.jar /merchant-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/merchant-0.0.1-SNAPSHOT.jar"]