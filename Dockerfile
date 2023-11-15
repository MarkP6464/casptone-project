FROM openjdk:17-oracle
COPY ./target/capstone-project-0.0.1-SNAPSHOT.war /usr/local/lib/demo.jar

ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
