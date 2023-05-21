# Rock Paper Scissors game microservices

The Rock Paper Scissors game project provides infrastructure, REST and gRPC APIs for the Rock Paper Scissors game.

* [Rock Paper Scissors game command microservice](https://github.com/hokushin118/rps-microservices/tree/master/microservices/rps-cmd-service)
* [Rock Paper Scissors game query microservice](https://github.com/hokushin118/rps-microservices/tree/master/microservices/rps-qry-service)
* [Score command microservice](https://github.com/hokushin118/rps-microservices/tree/master/microservices/score-cmd-service)
* [Score query microservice](https://github.com/hokushin118/rps-microservices/tree/master/microservices/score-cmd-service)

### Prerequisites

* Docker Desktop
* Java 11 or higher

### Technology stack

* [OpenJDK 11](https://openjdk.java.net/projects/jdk/11)
* [Maven 3.6.0](https://maven.apache.org)
* [Spring Boot 2.6.1](https://spring.io/projects/spring-boot)
* [Lombok 1.18.20](https://projectlombok.org)
* [MapStruct](https://mapstruct.org)
* [Apache ZooKeeper 3.5.9](https://zookeeper.apache.org)
* [Apache Kafka 2.8.1](https://spring.io/projects/spring-kafka)
* [MongoDB NoSQL 3.0](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html)
* [MariaDB RDBMS 2.7.4](https://mariadb.org)
* [H2 Database Engine](https://www.h2database.com)
* [OpenAPI 3.0](https://springdoc.org)
* [gRPC framework 1.32.1](https://grpc.io/docs/languages/java/quickstart)
* [Hibernate Validator](https://hibernate.org/validator/)
* [Micrometer 1.8.0](https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector)
* [JUnit 5.8.2](https://junit.org/junit5/docs/current/user-guide)
* [Mockito 3.9.0](https://site.mockito.org)
* [Spock 2.1](https://spockframework.org)
* [Apache Groovy 3.0.9](https://groovy-lang.org)
* [JaCoCo](https://www.jacoco.org/jacoco) - unit and integration test coverage
* [Flyway](https://flywaydb.org) - database version control
* [Prometheus](https://prometheus.io) - metrics database
* [Grafana](https://grafana.com) - metrics visualization
* [ELK Stack](https://www.elastic.co) - log aggregation and monitoring in a centralized way
* [Redis](https://redis.io) - cache management

** H2 in-memory database engine is used for dev and it profiles only

### 1. Installation of Docker Desktop

* If [Docker Desktop](https://www.docker.com/products/docker-desktop) is not already installed navigate to the docker
  website download and install it on your local machine.
* To check if it is installed, you can simply run the command:

```
    > docker -v
```

* [Docker Compose](https://docs.docker.com/compose/install) is also required. To check if it is installed, you can
  simply run the command:

```
    > docker compose version
```

* If it is not already installed navigate to the [Docker Compose](https://docs.docker.com/compose/install) website and
  install it on your local machine.
* Check if Docker Swarm mode is active. To check it, you can simply run the command:

```
    > docker info
```

* If it is not already active, you can simply run the command:

```
    > docker swarm init
```

to activate it.

### 2. Deployment of the infrastructure

* Navigate to the root directory of the RPS Game project on your computer and run the docker compose command below to
  deploy necessary infrastructure on docker containers in the background.

```
     > docker compose -f docker-compose-general.yml -f docker-compose-kafka.yml -f docker-compose-metrics.yml -f docker-compose-api.yml up -d
```

* Verify that all necessary infrastructure and metrics containers are up and running.

```
    > docker compose -f docker-compose-general.yml -f docker-compose-kafka.yml -f docker-compose-metrics.yml -f docker-compose-api ps
```

* Navigate to the health checker microservice:

```
    > localhost/status/hc-ui
```

and make sure that all the RPS game microservices are up and running.

![health-checks](img/hc.png)
** Status gets refreshed every 10 seconds

### 3. Building of the necessary common libraries

* Navigate to the common/rps-grpc-lib directory on your computer.

```
    > cd common/rps-grpc-lib
```

* And run "mvn clean install" in the root directory of the rps-grpc-lib project to generate Java model classes and
  service descriptions for microservices from proto3 models.

```
     > mvn clean install
```

* Navigate to the common/cqrs-es-framework directory on your computer.

```
    > cd common/cqrs-es-framework
```

* And run "mvn clean install" in the root directory of the cqrs-es-framework project to create jar file and install it
  to local .m2 repository.

```
     > mvn clean install
```

* Navigate to the common/rps-common-lib directory on your computer.

```
    > cd common/rps-common-lib
```

* And run "mvn clean install" in the root directory of the rps-common-lib project to create jar file and install it to
  local .m2 repository.

```
     > mvn clean install
```

### 4. Running the RPS game command microservice from the command line

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
  http://localhost:8081/rps-cmd-api/swagger-ui/index.html or
  http://host.docker.internal/rps-cmd-api/swagger-ui/index.html
```

Note: NGINX is used as API gateway so if you deploy the microservices on docker containers you should remove port number
from the url.

### 5. Running the RPS game query microservice from the command line

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
  http://localhost:8082/rps-qry-api/swagger-ui/index.html or
  http://host.docker.internal/rps-qry-api/swagger-ui/index.html
```

### 6. Running the Score command microservice from the command line

* Navigate to the root directory of the microservice on your computer.

```
    > cd microservices/score-cmd-service
```

* Run "mvn clean package -P<profile>" in the root directory to create the Score Command microservice app.

```
     > mvn clean package -Pdev
```

* Run microservice from the command line using spring boot maven plugin. Run "mvn spring-boot:run
  -Dspring.profiles.active=<profile>" in the root directory of the microservice to launch the Score Command microservice
  app.

```
     > mvn spring-boot:run -Dspring.profiles.active=dev
```

* Or run microservice from the command line. Run "java -jar target/score-cmd-service.jar" in the root directory of the
  microservice to launch the Score Command microservice app.

```
     > java -jar target/score-cmd-service.jar
```

* Open any browser and navigate to the microservice Open API 3.0 definition (REST API).

```
  http://localhost:8083/score-cmd-api/swagger-ui/index.html or
  http://host.docker.internal/score-cmd-api/swagger-ui/index.html
```

### 7. Running the Score query microservice from the command line

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
  http://localhost:8084/score-qry-api/swagger-ui/index.html  or
  http://host.docker.internal/score-qry-api/swagger-ui/index.html
```

### Kubernetes (K8S)

Make sure that k8s is enabled in the Docker Desktop. If not, click on the __Settings__ icon, then on the __Kubernetes__ tab and check the __Enable Kubernetes__
checkbox. 

![enable_kubernetes](img/desktop-docker-k8s.png)

You can also use [minikube](https://minikube.sigs.k8s.io/docs/start) for local K8S development. 

```
     > minikube start \
            --addons=ingress,dashboard \
            --cni=flannel \
            --install-addons=true \
                --kubernetes-version=stable \
                --vm-driver=docker --wait=false \
                --cpus=4 --memory=6g --nodes=1 \
                --extra-config=apiserver.service-node-port-range=1-65535 \
            --embed-certs \
            --no-vtx-check \
            --docker-env HTTP_PROXY=https://minikube.sigs.k8s.io/docs/reference/networking/proxy/
```

Open a __Command Prompt__ and check if access is available for your Docker Desktop cluster:

```
     > kubectl cluster-info
```

Check the state of your Docker Desktop cluster:

```
     > kubectl get nodes
```

You should see a single node in the output called _docker-desktop_. That’s a full k8s cluster, with a single
node.

To create a project namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/dev/namespaces/rps-app-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

### Elasticsearch, Logstash, and Kibana (ELK Stack) on K8S cluster

There are several ways we can implement the __ELK Stack__ architecture pattern:

1. Beats —> Elasticsearch —> Kibana 

2. Beats —> Logstash —> Elasticsearch —> Kibana 

3. Beats —> Kafka —> Logstash —> Elasticsearch —> Kibana 

Here we implement the first approach. The last one is the better option for production environment.

#### 1. Creating namespace for ELK services

To create a kube-elk namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/dev/namespaces/kube-elk-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

You should see the following output:

```
      NAME                   STATUS   AGE     LABELS
      default                Active   2d13h   kubernetes.io/metadata.name=default
      ingress-nginx          Active   2d13h   app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx,kubernetes.io/metadata.name=ingress-nginx
      kube-elk               Active   2d12h   kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-node-lease        Active   2d13h   kubernetes.io/metadata.name=kube-node-lease
      kube-public            Active   2d13h   kubernetes.io/metadata.name=kube-public
      kube-system            Active   2d13h   kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   2d13h   addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying Elasticsearch cluster

_Elasticsearch is used for storing and searching logs._

To deploy elasticsearch cluster to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/dev/rbacs/elasticsearch-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/dev/services/elasticsearch-svc.yml
```

And then run:

```
     > kubectl apply -f ./k8s/dev/sets/elasticsearch-statefulset.yml
```

To monitor the deployment status, run:

```
     > kubectl rollout status sts/elasticsearch-sts -n kube-elk
```

To check the pod status, run:

```
     > kubectl get pods -n kube-elk
```

You should see the following output:

```
      NAME                                   READY   STATUS    RESTARTS         AGE
      elasticsearch-sts-0                    1/1     Running   0                16m
      elasticsearch-sts-1                    1/1     Running   0                15m
      elasticsearch-sts-2                    1/1     Running   0                15m
```

To check the state of the deployment, send an HTTP GET request using curl with this command:

```
     > curl localhost:9200
```

You may also check the health of your Elasticsearch cluster with this command:

```
     > curl localhost:9200/_cluster/health?pretty
```

You should see the following output:

```
    {
      "cluster_name" : "k8s-logs",
      "status" : "green",
      "timed_out" : false,
      "number_of_nodes" : 1,
      "number_of_data_nodes" : 1,
      "active_primary_shards" : 0,
      "active_shards" : 0,
      "relocating_shards" : 0,
      "initializing_shards" : 0,
      "unassigned_shards" : 0,
      "delayed_unassigned_shards" : 0,
      "number_of_pending_tasks" : 0,
      "number_of_in_flight_fetch" : 0,
      "task_max_waiting_in_queue_millis" : 0,
      "active_shards_percent_as_number" : 100.0
    }
```

You may also check the state of your Elasticsearch cluster with this command:

```
     > curl localhost:9200/_cluster/state?pretty
```

You may also check the log of your Elasticsearch cluster pod with this command:

```
     > kubectl logs <pod name> -n kube-elk
```

Or container inside your Elasticsearch cluster pod with this command:

```
     > kubectl logs <pod name> -c <container name> -n kube-elk
```

#### 3. Deploying Filebeat

_Filebeat is used to farm all the logs from all our nodes and pushing them to Logstash._

To deploy Filebeat to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/dev/rbacs/filebeat-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/dev/configmaps/filebeat-configmap.yml
```

And then run:

```
     > kubectl apply -f ./k8s/dev/sets/filebeat-daemonset.yml
```

Verify that the Filebeat DaemonSet rolled out successfully using kubectl:

```
     > kubectl get ds -n kube-elk
```

You should see the following status output:

```
      NAME           DESIRED   CURRENT   READY   UP-TO-DATE   AVAILABLE   NODE SELECTOR   AGE
      filebeat-dst   1         1         1       1            1           <none>          135m
```

To verify that Elasticsearch is indeed receiving this data, query the Filebeat index with this command:

```
     > curl http://localhost:9200/filebeat-*/_search?pretty
```

#### 4. Deploying Kibana

_Kibana is used for viewing logs._

To deploy Kibana to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/dev/configmaps/kibana-configmap.yml
```

Then run:

```
     > kubectl apply -f ./k8s/dev/services/kibana-svc.yml
```

And then run:

```
     > kubectl apply -f ./k8s/dev/deployments/kibana-deployment.yml
```

To access the Kibana interface, we have to forward a local port to the Kubernetes node running Kibana:

```
     > kubectl port-forward <kibana pod> 5601:5601 - n kube-elk
```

To check the state of the deployment, perform the following request against the Elasticsearch REST API:

```
     > curl localhost:9200/_cat/indices?v 
```

You should see the following output:

```
      health status index                      uuid                   pri rep docs.count docs.deleted store.size pri.store.size
      green  open   .kibana                    fP_HM1riQWGKpkl8FuGFTA   1   0          2            0     10.4kb         10.4kb
      green  open   .kibana_1                  g2SMz8XjShmSzTwmOQu9Fw   1   0          0            0       261b           261b
      green  open   .kibana_2                  2Poc2zmRRwawJNBO8Xeamg   1   0          0            0       261b           261b
      green  open   .kibana_task_manager       RgTFfA6lQ_CoSVUW8NbZGQ   1   0          2            0     19.2kb         19.2kb
      green  open   filebeat-6.8.23-2023.05.20 EUSLOZMWQGSyWMrh2EJiRA   5   0     122481            0     34.2mb         34.2mb
```

__Note:__ If you are running a single node cluster (Docker Desktop or MiniKube) you might need to perform the following request against the Elasticsearch REST API::

```
     > curl --location --request PUT 'localhost:9200/_settings' \
       --header 'Content-Type: application/json' \
       --data '{
           "index": {
               "number_of_replicas": 0
           }
       }'
```

And then access the Kibana UI in any browser:

```
     > http://localhost:5601
```

Navigate to the Kibana dashboard and in the __Discovery__ page, in the search bar enter:

```
     > kubernetes.pod_name:<name of the pod>
```

You should see a list of log entries for the specified pod.

#### 5. Deploying CronJob

_Elasticsearch cron job is used for clearing Elasticsearch indices._

To deploy Elasticsearch cron job to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/dev/configmaps/curator-configmap.yml
```

And then run:

```
     > kubectl apply -f ./k8s/dev/cronjobs/curator-cronjob.yml
```

Verify that the Elasticsearch cron job rolled out successfully using kubectl:

```
     > kubectl get cronjobs -n kube-elk
```

You should see the following status output:

```
      NAME              SCHEDULE    SUSPEND   ACTIVE   LAST SCHEDULE   AGE
      curator-cronjob   0 0 1 * *   False     0        <none>          22m

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

To get an idea of HTTP/2 performance, you can follow the link below:

* [HTTP/2 Demo](http://www.http2demo.io)

Kubernetes

* [K8S Cluster-level Logging Architecture](https://kubernetes.io/docs/concepts/cluster-administration/logging/#using-a-node-logging-agent)
* [K8S Dashboard](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard)
* [K8S Ingress Installation Guide](https://kubernetes.github.io/ingress-nginx/deploy/#quick-start)

ELK

* [Elastic Stack Guidelines](https://www.elastic.co/guide/index.html)
* [The Beats Family](https://www.elastic.co/beats)
* [Online Grok Pattern Generator / Debugger Tool](https://www.javainuse.com/grok)

### Microservice patterns used

* [Domain Driven Design (DDD)](https://en.wikipedia.org/wiki/Domain-driven_design)
* [Command Query Responsibility Segregation (CQRS)](https://microservices.io/patterns/data/cqrs.html)
* [Event sourcing (ES)](https://microservices.io/patterns/data/event-sourcing.html)
* [API Gateway](https://microservices.io/patterns/apigateway.html)
* [Gateway routing](https://docs.microsoft.com/en-us/azure/architecture/patterns/gateway-routing)
* [Gateway offloading](https://docs.microsoft.com/en-us/azure/architecture/patterns/gateway-offloading)
* [Database per service](https://microservices.io/patterns/data/database-per-service.html)
* [Polyglot persistence](https://martinfowler.com/bliki/PolyglotPersistence.html)
* [Bounded context](https://martinfowler.com/bliki/BoundedContext.html)
* [Domain event](https://microservices.io/patterns/data/domain-event.html)
* [Service instance per container](https://microservices.io/patterns/deployment/service-per-container.html)
* [Health Check API](https://microservices.io/patterns/observability/health-check-api.html)
* [Application metrics](https://microservices.io/patterns/observability/application-metrics.html)
* [Messaging](https://microservices.io/patterns/communication-style/messaging.html)
* [Idempotent consumer](https://microservices.io/patterns/communication-style/idempotent-consumer.html)
* [Server side discovery](https://microservices.io/patterns/server-side-discovery.html)
* [Log aggregation](https://microservices.io/patterns/observability/application-logging.html)

### Useful Docker commands

* When we don't need infrastructure containers anymore, we can take down containers and delete their corresponding
  volumes using the down command below:

```
     > docker compose -f docker-compose-general.yml -f docker-compose-kafka.yml -f docker-compose-metrics.yml -f docker-compose-elk.yml down -v
```

### BloomRPC Configuration

* Launch the BloomRPC application.
* Add path to proto folder of the rps-grpc-lib project to BloomRPC paths:

![add new path](img/bloomrpc-import-paths.png)

* Add testing service definition proto file:

![add server definition](img/bloomrpc-add-protos.png)

* Add gRPC server url and port:

![add gRPC server url and port](img/bloomrpc-add-url-port.png)

* Query the gRPC service:

![query gRPC services](img/bloomrpc-run-query.png)

### Apache Bench for microservice performance testing

* Download [Apache Bench](https://httpd.apache.org/download.cgi) tool on your computer.
* Launch CLI and type ab -n 1000 -c 10 http://<url of the microservice>:<port of the microservice>/contect path and
  endpoint/ to benchmark microservice.

```
> ab -n 1000 -c 10  http://127.0.0.1:8083/score-qry-api/v1/scores
```

-n 1000 is the number of requests to perform for the benchmarking session. The default is to just perform a single
request which usually leads to non-representative benchmarking results. -c 10 is the concurrency and denotes the number
of multiple requests to perform at a time. Default is one request at a time.
