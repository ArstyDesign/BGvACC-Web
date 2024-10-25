# Използваме Tomcat 9 с Java 1.8
FROM tomcat:9.0-jdk8

# Копираме .war файла в директорията на Tomcat
COPY target/BGvACC##*.war /usr/local/tomcat/webapps/ROOT.war

# Настройваме променливите на средата за PostgreSQL
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=bgvacc_test
ENV DB_USER=bgvacc_user
ENV DB_PASSWORD=BGvACC-TestApp2024

# Излагаме порт 8080, на който работи Tomcat
EXPOSE 8080

# Започваме Tomcat
CMD ["catalina.sh", "run"]
