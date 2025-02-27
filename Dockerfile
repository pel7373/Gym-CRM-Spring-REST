FROM tomcat:11.0-jdk21

COPY target/root.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]