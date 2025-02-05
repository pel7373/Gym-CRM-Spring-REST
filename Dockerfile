FROM openjdk:21-jdk-slim

WORKDIR /usr/local/tomcat/webapps/

COPY /target/root.war /usr/local/tomcat/webapps/