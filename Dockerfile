FROM eclipse-temurin:17-jdk-jammy
LABEL authors="andreypinchuk"

VOLUME /tmp
COPY target/*.jar KishkaStrybaieApplication.jar
ENTRYPOINT ["java","-jar","/KishkaStrybaieApplication.jar"]