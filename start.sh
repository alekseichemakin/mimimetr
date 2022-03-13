./mvnw clean package -DskipTests
cp target/mimimetr-0.0.1-SNAPSHOT.jar src/main/docker
docker-compose -f src/main/docker/docker-compose.yml down
docker rmi docker-spring-boot-postgres:latest
docker-compose -f src/main/docker/docker-compose.yml up