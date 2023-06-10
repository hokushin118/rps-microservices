# Score query microservice

The Score query microservice provides REST and gRPC APIs for the Rock Paper Scissors game scores.

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
* [Spock 2.1](https://spockframework.org)
* [Apache Groovy 3.0.9](https://groovy-lang.org)
* [JaCoCo test coverage](https://www.jacoco.org/jacoco)
* [Flyway db version control](https://flywaydb.org)
  ** H2 in-memory database engine is used for dev and it profiles only

### Preconditions for running microservice

* Make sure you have the infrastructure is up and running before you run Score query microservice.
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

### Running the Score query microservice from the command line

* Navigate to the root directory of the microservice on your computer.

```
    > cd microservices/score-qry-service
```

* Run "mvn clean package -P<profile>" in the root directory to create the Score Query microservice app.

```
     > mvn clean package -Pdev
```

* Run microservice from the command line using spring boot maven plugin. Run "mvn spring-boot:run
  -Dspring.profiles.active=<profile>" in the root directory of the microservice to launch the Score Query microservice
  app.

```
     > mvn spring-boot:run -Dspring.profiles.active=dev
```

* Or run microservice from the command line. Run "java -jar target/score-qry-service.jar" in the root directory of the
  microservice to launch the Score Query microservice app.

```
     > java -jar target/score-qry-service.jar
```

* Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
  http://localhost:8084/score-qry-api/swagger-ui/index.html
```

* Open any browser and navigate to the microservice Actuator. Actuator endpoints let you monitor and interact with the microservice. All endpoints are exposed over HTTP under dev profile.

```
  http://localhost:8084/score-qry-api/actuator
```

Note: NGINX is used as API gateway so if you deploy the microservices on docker containers you should remove port number from the url.

### Deploying the Score query microservice on K8S cluster

#### 1. Deploying the Score query microservice

To deploy the Score query microservice to Kubernetes, first deploy the microservice K8S config map with the following command:

```
     > kubectl apply -f ./k8s/configmaps/score-qry-service-configmap.yml
```

Then deploy the microservice K8S service with the following command:

```
     > kubectl apply -f ./k8s/services/score-qry-service-svc.yml
```

To check the service deployment status, run:

```
     > kubectl get services -n rps-app-dev
```

You should see the following output:

```
      NAME                         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)     AGE
      score-qry-service-grpc-svc   ClusterIP   10.109.128.64    <none>        50051/TCP   8s
      score-qry-service-svc        ClusterIP   10.103.139.184   <none>        8080/TCP    8s
```

Then deploy the microservice K8S secret with the following command:

```
     > kubectl apply -f ./k8s/secrets/score-qry-service-secret.yml
```

Now the secrets can be referenced in our deployment.

And then deploy the Score query microservice with the following command:

```
     > kubectl apply -f ./k8s/deployments/score-qry-service-deployment.yml
```

To check the pod status, run:

```
     > kubectl get pods -n rps-app-dev -o wide
```

You should see the following output:

```
      NAME                                            READY   STATUS    RESTARTS   AGE    IP            NODE       NOMINATED NODE   READINESS GATES
      score-qry-service-deployment-7b4fbc8f6-mhb5f    1/1     Running   0          17m    10.244.3.32   minikube   <none>           <none>
```

You may also check the logs for any of the Score query microservice pods with this command:

```
     > kubectl logs <pod name> -c score-qry-service -n rps-app-dev
```

and ensure that you see lines similar to the ones shown below, which confirm the microservice is up and running:

```
      {"@timestamp":"2023-06-03T15:05:17.562Z","@version":"1","level":"INFO","message":"gRPC Server started, listening on address: *, port: 50051","logger_name":"net.devh.boot.grpc.server.serverfactory.GrpcServerLifecycle","thread_name":"main"}
      ...
      {"@timestamp":"2023-06-03T15:05:17.836Z","@version":"1","level":"INFO","message":"score-qry-service has successfully been started...","logger_name":"com.al.qdt.score.qry.ScoreQryServiceApp","thread_name":"main"}
```

Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
     > http://rps.internal/score-qry-api
```

#### 2. Verifying REST API

Verify the REST API with the following command:

```
      > curl -k -v --location 'rps.internal/score-qry-api/v1/scores' --header 'Accept: application/json' --header 'Content-Type: application/json'
```

__Note:__ -k flag is used to skip self-signed certificate verification, -v flag is used to get verbose fetching.

You should see the following output:

```
      [ {
        "id" : "17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc",
        "winner" : "DRAW"
      }, {
        "id" : "598587f5-c2f4-4520-b669-2e0e0d9d9e25",
        "winner" : "MACHINE"
      },
      ...
      ]
```

#### 3. Verifying gRPC API

Make sure that [gRPC reflection](https://github.com/grpc/grpc/blob/master/doc/server-reflection.md) is enabled in the _score-qry-service-configmap.yml_ configuration file of the microservice:

```
      grpc-reflection-service-enabled: on
```

__Note:__  Turn off the grpc service listing on production environment.

To list all available grpc services exposed by the gRPC server of the RPS Game Command microservice, execute the following command:

```
      > grpcurl -insecure grpc.score.qry.internal:443 list
```

You should see the following output:

```
      grpc.health.v1.Health
      grpc.reflection.v1alpha.ServerReflection
      v1.services.ScoreQryService
```

To conduct a gRPC server health check, execute the following command:

```
      > grpcurl -insecure grpc.score.qry.internal:443 grpc.health.v1.Health/Check
```

You should see the following output:

```
      {
        "status": "SERVING"
      }
```

It means that gRPC server of the RPS Game Command microservice is up and running.

To get all methods of the specified service, execute the following command:

```
      > grpcurl -insecure grpc.score.qry.internal:443 list v1.services.ScoreQryService
```

You should see the following output:

```
      v1.services.ScoreQryService.findById
      v1.services.ScoreQryService.findByWinner
      v1.services.ScoreQryService.listOfScores
```

To get more details about a grpc service execute the following command:

```
      > grpcurl -insecure grpc.score.qry.internal:443 describe v1.services.ScoreQryService
```

You should see the following output:

```
      service ScoreQryService {
        rpc findById ( .v1.services.FindScoreByIdRequest ) returns ( .v1.services.ScoreResponse );
        rpc findByWinner ( .v1.services.FindScoreByWinnerRequest ) returns ( .v1.services.ListOfScoresResponse );
        rpc listOfScores ( .v1.services.ListOfScoresRequest ) returns ( .v1.services.ListOfScoresResponse );
      }
```

Verify the gRPC API play method with the following command:

```
      >  grpcurl -d '{}' -insecure grpc.score.qry.internal:443 v1.services.ScoreQryService/listOfScores
```

You should see the following output:

```
{
  "scores": [
    {
      "id": "17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc",
      "winner": "DRAW"
    },
    {
      "id": "598587f5-c2f4-4520-b669-2e0e0d9d9e25",
      "winner": "MACHINE"
    },
    ...
  ]
}
```

It means that gRPC API is up and running.

#### 4. Deploying HPA for pods

Now, let's deploy a HorizontalPodAutoscaler (HPA) for the Score Query microservice.
To deploy the HPA for the microservice, run the following command:

```
     > kubectl apply -f ./k8s/hpas/score-qry-service-hpa.yml
```

__Note:__ HPA is a form of autoscaling that increases or decreases the number of pods in a replication controller, deployment, replica set, or stateful set based on cpu utilization, number of requests etc.

Verify the HPA deployment with the following command:

```
     > kubectl get hpa -n rps-app-dev
```

You should see the following output:

```
      NAME                    REFERENCE                                 TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
      score-qry-service-hpa   Deployment/score-qry-service-deployment   1%/50%    1         5         1          74s
```

The current HPA configuration would attempt to ensure that each pod was consuming roughly 50% of its requested CPU.

[HorizontalPodAutoscaler Walkthrough](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough)

You can easily [scale up](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#scaling-a-deployment) the microservice deployment by executing the following command:

```
     > kubectl scale deployment/score-qry-service-deployment --replicas=3 -n rps-app-dev
```

#### 5. Sending metrics from the Score query microservice to the Monitoring Stack

To send the Score query microservice metrics to the Monitoring Stack, first make sure that the Prometheus endpoint is configured in the application as below:

[Gathering Metrics from Spring Boot on Kubernetes with Prometheus and Grafana](https://tanzu.vmware.com/developer/guides/spring-prometheus)

Then deploy the microservice Prometheus service monitor with the following command:

```
     > kubectl apply -f ./k8s/sms/score-qry-service-sm.yml
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
