# Rock Paper Scissors game command microservice

The Rock Paper Scissors game command microservice provides REST and gRPC APIs for the Rock Paper Scissors game.

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
* [MongoDB 3.0](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html)
* [OpenAPI 3.0](https://springdoc.org)
* [gRPC framework 1.32.1](https://grpc.io/docs/languages/java/quickstart)
* [Micrometer 1.8.0](https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector)
* [JUnit 5.8.2](https://junit.org/junit5/docs/current/user-guide)
* [Mockito 3.9.0](https://site.mockito.org)
* [JaCoCo test coverage](https://www.jacoco.org/jacoco)

### Preconditions for running microservice

* Make sure you have the infrastructure is up and running before you run RPS command microservice.
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

### Running the RPS game command microservice from the command line

* Navigate to the root directory of the microservice on your computer.

```
    > cd microservices/rps-cmd-service
```

* Run "mvn clean package -P<profile>" in the root directory to create the Rock Paper Scissors Game Command microservice
  app.

```
     > mvn clean package -Pdev
```

* Run microservice from the command line using spring boot maven plugin. Run "mvn spring-boot:run
  -Dspring.profiles.active=<profile>" in the root directory of the microservice to launch the Rock Paper Scissors Game
  Command microservice app.

```
     > mvn spring-boot:run -Dspring.profiles.active=dev
```

* Or run microservice from the command line. Run "java -jar target/rps-cmd-service.jar" in the root directory of the
  microservice to launch the Rock Paper Scissors Game Command microservice app.

```
     > java -jar target/rps-cmd-service.jar
```

* Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
  http://localhost:8081/rps-cmd-api/swagger-ui/index.html
```

* Open any browser and navigate to the microservice Actuator. Actuator endpoints let you monitor and interact with the microservice. All endpoints are exposed over HTTP under dev profile.

```
  http://localhost:8081/rps-cmd-api/actuator
```

Note: NGINX is used as API gateway so if you deploy the microservices on docker containers you should remove port number from the url.

### Installing the RPS game command microservice on K8S cluster

#### 1. Creating namespace for RPS game microservices (if not exists)

To create a _rps-app-dev_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/dev/namespaces/rps-app-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

#### 2. Deploying Simple Fanout Ingress for RPS microservices (if not exists)

To create a [Simple Fanout Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) for the RPS microservices, run:

```
     > kubectl apply -f ./k8s/dev/ingress/rps-ingress.yml
```

__Note:__ A RPS application [Simple Fanout Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) configuration routes traffic from a single IP address to more than one.

![Simple Fanout Ingress](https://d33wubrfki0l68.cloudfront.net/36c8934ba20b97859854610063337d2072ea291a/28e8b/docs/images/ingressfanout.svg)

Make sure the RPS application ingress has been created:

```
     > kubectl get ingress -n rps-app-dev
```

You should see the following output:

```
      NAME                    CLASS   HOSTS                    ADDRESS        PORTS   AGE
      rps-ingress             nginx   rps.internal             192.168.49.2   80      40h
```

Note the ip address (192.168.49.2) displayed in the output, as you will need this in the next step.

#### 3. Adding custom entry to the etc/host file for the RPS game microservices (if not exists)

Add a custom entry to the etc/hosts file using the nano text editor:

```
     > sudo nano /etc/hosts
```

You should add the following ip address (copied in the previous step) and custom domain to the hosts file:

```
      192.168.49.2 rps.internal
```

You may check the custom domain name with ping command:

```
     > ping rps.internal
```

You should see the following output:

```
      64 bytes from rps.internal (192.168.49.2): icmp_seq=1 ttl=64 time=0.072 ms
      64 bytes from rps.internal (192.168.49.2): icmp_seq=2 ttl=64 time=0.094 ms
      64 bytes from rps.internal (192.168.49.2): icmp_seq=3 ttl=64 time=0.042 ms
```

#### 4. Deploying the RPS game command microservice

To deploy the RPS game command microservice to Kubernetes, first deploy the microservice K8S config map with the following command:

```
     > kubectl apply -f ./k8s/dev/configmaps/rps-cmd-service-configmap.yml
```

Then deploy the microservice K8S service with the following command:

```
     > kubectl apply -f ./k8s/dev/services/rps-cmd-service-svc.yml
```

To check the service deployment status, run:

```
     > kubectl get services -n rps-app-dev
```

Then deploy the microservice K8S secret with the following command:

```
     > kubectl apply -f ./k8s/dev/secrets/rps-cmd-service-secret.yml
```

Now the secrets can be referenced in our deployment.

And then deploy the RPS game command microservice with the following command:

```
     > kubectl apply -f ./k8s/dev/deployments/rps-cmd-service-deployment.yml
```

To check the pod status, run:

```
     > kubectl get pods -n rps-app-dev -o wide
```

You should see the following output:

```
      NAME                                          READY   STATUS    RESTARTS   AGE    IP            NODE       NOMINATED NODE   READINESS GATES
      rps-cmd-service-deployment-657d798b67-qsbqz   1/1     Running   0          154m   10.244.3.13   minikube   <none>           <none>
```

You may also check the logs for any of the RPS game command microservice pods with this command:

```
     > kubectl logs <pod name> -c rps-cmd-service -n rps-app-dev
```

and ensure that you see lines similar to the ones shown below, which confirm the microservice is up and running:

```
      {"@timestamp":"2023-05-30T20:09:28.868Z","@version":"1","level":"INFO","message":"rps-cmd-service has successfully been started...","logger_name":"com.al.qdt.rps.cmd.RpsCmdServiceApp","thread_name":"main"}
```

Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
     > http://rps.internal/rps-cmd-api/swagger-ui/index.html
```

Verify the REST API with the following command:

```
    > curl --location 'rps.internal/rps-cmd-api/v1/games' --header 'Accept: application/json' --header 'Content-Type: application/json' --data '{
        "id": "748873ec-f887-4090-93ff-f8b8cbb34c7a",
        "username": "User1",
        "hand": "ROCK"
      }'
```

You should see the following output:

```
      {"result":"DRAW","user_choice":"PAPER","machine_choice":"PAPER"}
```

Connect to the first (primary) replica set member shell with the following command:

```
     > kubectl -n kube-nosql-db exec -it mongodb-sts-0 -- mongo
```

And execute the following commands:

```
      rs0:PRIMARY> use rpsDB
      switched to db rpsDB
      rs0:PRIMARY> db.events.find().pretty()
```

You should see the following output:

```
      {
              "_id" : ObjectId("647a2e58deda4f5536e24338"),
              "aggregate_id" : UUID("17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc"),
              "aggregate_type" : "com.al.qdt.rps.cmd.domain.aggregates.RpsAggregate",
              "event_type" : "com.al.qdt.common.events.rps.GamePlayedEvent",
              "event_data" : {
                      "_id" : UUID("17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc"),
                      "username" : "User1",
                      "hand" : "PAPER",
                      "_class" : "com.al.qdt.common.events.rps.GamePlayedEvent"
              },
              "played" : ISODate("2023-06-02T18:00:56.874Z"),
              "version" : 0,
              "created" : ISODate("2023-06-02T18:00:56.874Z"),
              "_class" : "event"
      }
      {
              "_id" : ObjectId("647a2e58deda4f5536e24339"),
              "aggregate_id" : UUID("17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc"),
              "aggregate_type" : "com.al.qdt.rps.cmd.domain.aggregates.RpsAggregate",
              "event_type" : "com.al.qdt.common.events.score.ScoresAddedEvent",
              "event_data" : {
                      "_id" : UUID("17f8ef5a-bf3b-4b7b-9c51-a6019434e0bc"),
                      "winner" : "DRAW",
                      "_class" : "com.al.qdt.common.events.score.ScoresAddedEvent"
              },
              "played" : ISODate("2023-06-02T18:00:56.915Z"),
              "version" : 0,
              "created" : ISODate("2023-06-02T18:00:56.915Z"),
              "_class" : "event"
      }
```

Two events has successfully been created.

#### 5. Deploying HPA for pods

Now, let's deploy a HorizontalPodAutoscaler (HPA) for the RPS Game Command microservice.
To deploy the HPA for the microservice, run the following command:

```
     > kubectl apply -f ./k8s/dev/hpas/rps-cmd-service-hpa.yml
```

__Note:__ HPA is a form of autoscaling that increases or decreases the number of pods in a replication controller, deployment, replica set, or stateful set based on cpu utilization, number of requests etc.

Verify the HPA deployment with the following command:

```
     > kubectl get hpa -n rps-app-dev
```

You should see the following output:

```
      NAME                  REFERENCE                               TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
      rps-cmd-service-hpa   Deployment/rps-cmd-service-deployment   1%/50%    1         5         1          74s
```

The current HPA configuration would attempt to ensure that each pod was consuming roughly 50% of its requested CPU.

[HorizontalPodAutoscaler Walkthrough](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough)

You can easily [scale up](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#scaling-a-deployment) the microservice deployment by executing the following command:

```
     > kubectl scale deployment/rps-cmd-service-deployment --replicas=3 -n rps-app-dev
```

#### 6. Sending metrics from the RPS game command microservice to the Monitoring Stack

To send the RPS game command microservice metrics to the Monitoring Stack, first make sure that the Prometheus endpoint is configured in the application as below:

[Gathering Metrics from Spring Boot on Kubernetes with Prometheus and Grafana](https://tanzu.vmware.com/developer/guides/spring-prometheus)

Then deploy the microservice Prometheus service monitor with the following command:

```
     > kubectl apply -f ./k8s/dev/sms/rps-cmd-service-sm.yml
```

### Useful links

For testing gRPC API (make sure that you are using correct grpc port for a profile), please consider the following
options:

* [BloomRPC GUI client for gRPC](https://github.com/bloomrpc/bloomrpc)
* [gRPCurl command-line tool](https://github.com/fullstorydev/grpcurl)
* [gRPC UI command-line tool](https://github.com/fullstorydev/grpcui)

For testing REST API, you can also consider the following options:

* [Postman GUI client for REST](https://www.postman.com)

For testing MongoDB, you can also consider the following options:

* [Compass GUI for MongoDB](https://www.mongodb.com/products/compass)
* [Robo 3T for MongoDB](https://robomongo.org)
