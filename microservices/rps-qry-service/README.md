# Rock Paper Scissors game query microservice

The Rock Paper Scissors game query microservice provides REST and gRPC APIs for the Rock Paper Scissors game.

### Prerequisites

* Docker Desktop
* Java 11 or higher
* (Optional) IntelliJ IDEA

### Technology stack

* [OpenJDK 11](https://openjdk.java.net/projects/jdk/11)
* [Maven 3.6.0](https://maven.apache.org)
* [Spring Boot 2.6.1](https://spring.io/projects/spring-boot)
* [Lombok 1.18.20](https://projectlombok.org)
* [MapStruct](https://mapstruct.org)
* [Apache ZooKeeper 3.5.9](https://zookeeper.apache.org)
* [Apache Kafka 2.8.1](https://spring.io/projects/spring-kafka)
* [MariaDB RDBMS 2.7.4](https://mariadb.org)
* [H2 Database Engine](https://www.h2database.com)
* [OpenAPI 3.0](https://springdoc.org)
* [gRPC framework 1.32.1](https://grpc.io/docs/languages/java/quickstart)
* [Micrometer 1.8.0](https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector)
* [JUnit 5.8.2](https://junit.org/junit5/docs/current/user-guide)
* [Mockito 3.9.0](https://site.mockito.org)
* [JaCoCo test coverage](https://www.jacoco.org/jacoco)
* [Flyway db version control](https://flywaydb.org)
  ** H2 in-memory database engine is used for dev and it profiles only

### Preconditions for running microservice

* Make sure you have the infrastructure is up and running before you run RPS query microservice.
* If not navigate to the root directory of the project on your computer and run the docker compose command below to
  deploy necessary infrastructure on docker containers in the background.

```
     > docker compose -f docker-compose-general.yml -f docker-compose-kafka.yml -f docker-compose-metrics.yml -f docker-compose-elk.yml -f docker-compose-api.yml up -d
```

* If rps-grpc-lib is not already installed navigate to the common/rps-grpc-lib directory on your computer.

```
    > cd common/rps-grpc-lib
```

* And run "mvn clean install" in the root directory of the rps-grpc-lib project to generate Java model classes and
  service descriptions for microservices from proto3 models.

```
     > mvn clean install
```

* If cqrs-es-framework is not already installed navigate to the common/cqrs-es-framework directory on your computer.

```
    > cd common/cqrs-es-framework
```

* And run "mvn clean install" in the root directory of the cqrs-es-framework project to create jar file and install it
  to local .m2 repository.

```
     > mvn clean install
```

* If rps-common-lib is not already installed navigate to the common/rps-common-lib directory on your computer.

```
    > cd common/rps-common-lib
```

* And run "mvn clean install" in the root directory of the rps-common-lib project to create jar file and install it to
  local .m2 repository.

```
     > mvn clean install
```

### Running the RPS game query microservice from the command line

* Navigate to the root directory of the microservice on your computer.

```
    > cd microservices/rps-qry-service
```

* Run "mvn clean package -P<profile>" in the root directory to create the Rock Paper Scissors Game Query microservice
  app.

```
     > mvn clean package -Pdev
```

* Run microservice from the command line using spring boot maven plugin. Run "mvn spring-boot:run
  -Dspring.profiles.active=<profile>" in the root directory of the microservice to launch the Rock Paper Scissors Game
  Query microservice app.

```
     > mvn spring-boot:run -Dspring.profiles.active=dev
```

* Or run microservice from the command line. Run "java -jar target/rps-qry-service.jar" in the root directory of the
  microservice to launch the Rock Paper Scissors Query microservice app.

```
     > java -jar target/rps-qry-service.jar
```

* Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
  http://localhost:8082/rps-qry-api/swagger-ui/index.html
```

* Open any browser and navigate to the microservice Actuator. Actuator endpoints let you monitor and interact with the microservice. All endpoints are exposed over HTTP under dev profile.

```
  http://localhost:8082/rps-qry-api/actuator
```

Note: NGINX is used as API gateway so if you deploy the microservices on docker containers you should remove port number from the url.

### Installing the RPS game query microservice on K8S cluster

#### 1. Creating namespace for RPS game microservices

To create a rps-app-dev namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/dev/namespaces/rps-app-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

#### 2. Deploying the RPS game query microservice

To deploy the RPS game query microservice to Kubernetes, first deploy the microservice K8S service with the following command:

```
     > kubectl apply -f ./k8s/dev/services/rps-qry-service-svc.yml
```

To check the service deployment status, run:

```
     > kubectl get services -n rps-app-dev
```

Then deploy the microservice K8S secret with the following command:

```
     > kubectl apply -f ./k8s/dev/secrets/rps-qry-service-secret.yml
```

And then deploy the RPS game query microservice with the following command:

```
     > kubectl apply -f ./k8s/dev/deployments/rps-qry-service-deployment.yml
```

To check the pod status, run:

```
     > kubectl get pods -n rps-app-dev
```

To check the state of the deployment, first forward the RPS game query microservice to your local environment with the following command:

```
     > kubectl port-forward svc/rps-qry-service-svc 8082 -n rps-app-dev
```

And open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
     > http://localhost:8082/rps-qry-api/swagger-ui/index.html
```

You may also check the log of the RPS game query microservice with this command:

```
     > kubectl logs <pod name> -n rps-app-dev
```

### Useful links

For testing gRPC API (make sure that you are using correct grpc port for a profile), please consider the following
options:

* [BloomRPC GUI client for gRPC](https://github.com/bloomrpc/bloomrpc)
* [gRPCurl command-line tool](https://github.com/fullstorydev/grpcurl)
* [gRPC UI command-line tool](https://github.com/fullstorydev/grpcui)

For testing REST API, you can also consider the following options:

* [Postman GUI client for REST](https://www.postman.com)

For testing MariaDB, you can also consider the following options:

* [GUI tools for MongoDB](https://mariadb.com/kb/en/graphical-and-enhanced-clients)
