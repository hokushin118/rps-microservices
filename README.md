# Rock Paper Scissors game microservices

The Rock Paper Scissors game project provides infrastructure, REST and gRPC APIs for the Rock Paper Scissors game.

The Rock Paper Scissors game project uses [CQRS](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)
pattern. CQRS is an architectural pattern that can help maximize performance, scalability, and security. The pattern
separates operations that read data from those operations that write data.

Source: [Architecting Cloud Native .NET Applications for Azure](https://learn.microsoft.com/en-us/dotnet/architecture/cloud-native)

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

### 2. Deployment of the infrastructure ([backing services](https://12factor.net/backing-services))

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

***

From experience, Docker Compose is a great option for small-scale applications that don't require a lot of
infrastructure. It's easy to use and can be deployed quickly. It also a great tool for local development.

However, Docker Compose is not as scalable as Kubernetes and is not that suitable for developing large-scale
applications. Kubernetes is a more complex but more powerful deployment technique.

Docker Compose vs K8S, pros and cons:

[Docker Swarm vs Kubernetes: how to choose a container orchestration tool](https://circleci.com/blog/docker-swarm-vs-kubernetes)
[Kubernetes vs Docker: A comprehensive comparison](https://www.civo.com/blog/kubernetes-vs-docker-a-comprehensive-comparison)

***

### Kubernetes (K8S)

#### Prerequisites

Make sure that k8s is enabled in the Docker Desktop. If not, click on the __Settings__ icon, then on the __Kubernetes__
tab and check the __Enable Kubernetes__ checkbox.

![enable_kubernetes](img/desktop-docker-k8s.png)

You can also use [minikube](https://minikube.sigs.k8s.io/docs/start) for local K8S development.

Make sure Minikube, kubectl and helm are installed.

[kubectl installation](https://kubernetes.io/docs/tasks/tools/install-kubectl)
[Minikube installation](https://minikube.sigs.k8s.io/docs/start)
[Helm installation](https://helm.sh/docs/intro/install)
[How To Install Minikube on Ubuntu 22.04|20.04|18.04](https://computingforgeeks.com/how-to-install-minikube-on-ubuntu-debian-linux)
[How To Install Docker On Ubuntu 22.04 | 20.04](https://cloudcone.com/docs/article/how-to-install-docker-on-ubuntu-22-04-20-04)

Start minikube cluster:

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

__Note:__ The infrastructure clusters require significant resources (CPUs, memory). I have the following server
configuration:

```
     OS: Ubuntu 22.04.2 LTS (Jammy Jellyfish)
     Processor: Intel Xeon Processor (Icelake) 2GHz 16Mb
     vCPU: 4
     RAM: 32
```

Make sure Minikube is up and running with the following command:

```
     > minikube status
```

You should see the following output:

```
      minikube
      type: Control Plane
      host: Running
      kubelet: Running
      apiserver: Running
      kubeconfig: Configured
```

Verify that _metrics-server_ is installed by executing the following command:

```
      > minikube addons list | grep metrics-server
```

If not, you will see the following output:

```
      | metrics-server              | minikube | disabled     | Kubernetes   
```

To install _metrics-server_ on your K8S cluster, run:

```
      > minikube addons enable metrics-server
```

You should see the following output:

```
      You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
      - Using image registry.k8s.io/metrics-server/metrics-server:v0.6.3
      * The 'metrics-server' addon is enabled
```

Verify that _metrics-server_ pod is up and running:

```
      > kubectl get pods -n kube-system | grep metrics-server
```

You should see the following output:

```
      metrics-server-6588d95b98-bdb6x    1/1     Running   0             2m35s
```

It means that _metrics-server_ is up and running.

Now, if you run the following command:

```
      > kubectl top pod -n rps-app-dev
```

you will see resources used in specified namespace:

```
      NAME                                           CPU(cores)   MEMORY(bytes)
      rps-cmd-service-deployment-59bc84c8-bcx4b      1m           573Mi
      rps-qry-service-deployment-9b4fbc8f6-vw58g     3m           590Mi
      score-cmd-service-deployment-676c56db8-rpfbc   1m           389Mi
```

Now that you are certain everything is up and running deploy the Kubernetes Dashboard with the command:

```
     > minikube dashboard
```

If you want to access the K8S Dashboard from outside the cluster, run the following command:

```
      > kubectl proxy --address='0.0.0.0' --accept-hosts='^*$'
```

And then access the K8S Dashboard in any browser:

```
      http://<ip of your hosting server>:8001/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/#/workloads?namespace=default
```

Open a __Command Prompt__ and check if access is available for your Minikube cluster:

```
     > kubectl cluster-info
```

You should see the following output:

```
      Kubernetes control plane is running at https://192.168.49.2:8443
      CoreDNS is running at https://192.168.49.2:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
      
      To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

Check the state of your Minikube cluster:

```
     > kubectl get nodes
```

The output will list all of a cluster’s nodes and the version of Kubernetes each one is running.

```
      NAME       STATUS   ROLES           AGE     VERSION
      minikube   Ready    control-plane   7d14h   v1.26.3
```

You should see a single node in the output called _minikube_. That’s a full K8S cluster, with a single node.

First, we have to set up our infrastucture.

### Elasticsearch, Logstash and Kibana (ELK Stack) on K8S cluster

There are several ways we can implement the __ELK Stack__ architecture pattern:

1. __Beats__ —> __Elasticsearch__ —> __Kibana__

2. __Beats__ —> __Logstash__ —> __Elasticsearch__ —> __Kibana__

3. __Beats__ —> __Kafka__ —> __Logstash__ —> __Elasticsearch__ —> __Kibana__

Here we implement the first and second approaches. The last one is the better option for production environment cause
Kafka acts as a data buffer and helps prevent data loss or interruption while streaming files quickly.

#### 1. Creating namespace for ELK services

To create a _kube-elk_ namespace on the K8S cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-elk-ns.yml
```

__Note:__ In Kubernetes, [namespaces](https://kubernetes.io/docs/concepts/overview/working-with-objects/namespaces)
provides a mechanism for isolating groups of resources within a single cluster. However not all objects are in a
namespace.

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

_Elasticsearch is the core component of ELK. It works as a searchable database for log files._

To deploy elasticsearch cluster to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/rbacs/elasticsearch-rbac.yml
```

Then deploy a headless service for _Elasticsearch_ pods using the following command:

```
     > kubectl apply -f ./k8s/services/elasticsearch-svc.yml
```

__Note:__ You cannot directly access the application running in the pod. If you want to access the application, you need
a Service object in the Kubernetes cluster.

_Headless_ service means that only internal pods can communicate with each other. They are not exposed to external
requests outside the Kubernetes cluster. _Headless_ services should be used when client applications or pods want to
communicate with specific (not randomly selected) pod (stateful application scenarios).

And then run:

```
     > kubectl apply -f ./k8s/sets/elasticsearch-statefulset.yml
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

To access the Elasticsearch locally, we have to forward a local port 9200 to the Kubernetes node running Elasticsearch
with the following command:

```
     > kubectl port-forward <elasticsearch pod> 9200:9200 -n kube-elk
```

In our case:

```
     > kubectl port-forward elasticsearch-sts-0 9200:9200 -n kube-elk
```

You should see the following output:

```
      Forwarding from 127.0.0.1:9200 -> 9200
      Forwarding from [::1]:9200 -> 9200
```

The command forwards the connection and keeps it open. Leave the terminal window running and proceed to the next step.

In another terminal tab, test the connection with the following command:

```
     > curl localhost:9200
```

The output prints the deployment information.

Alternatively, access localhost:9200 from the browser. The output shows the cluster details in JSON format, indicating
the deployment is successful.

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

_Filebeat is used to farm all the logs from all our nodes and pushing them to Elasticsearch._

To deploy Filebeat to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/rbacs/filebeat-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/configmaps/filebeat-configmap.yml
```

__Note:__ If you are running the __Beats__ —> __Elasticsearch__ —> __Kibana__ scenario, go to the filebeat-configmap.yml
file and make the changes below before deploying:

```
    # Send events directly to Elasticsearch cluster
    output.elasticsearch:
     hosts: ['${FILEBEAT_ELASTICSEARCH_URL:elasticsearch-svc.kube-elk}']
     username: ${FILEBEAT_ELASTICSEARCH_USERNAME}
     password: ${FILEBEAT_ELASTICSEARCH_PASSWORD}

    # Send events to Logstash
    # output.logstash:
    #  hosts: ['${FILEBEAT_LOGSTASH_URL:logstash-svc.kube-elk}']
```

And then run:

```
     > kubectl apply -f ./k8s/sets/filebeat-daemonset.yml
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

You can also make sure that filebeat container is up and running by viewing logs:

```
     > kubectl logs <filebeat pod name> -c filebeat -n kube-elk -f
```

#### 4. Deploying Logstash

_Logstash is used for ingesting data from a multitude of sources, transforming it, and then sending it to
Elasticsearch._

__Note:__ Skip this step if you are running the __Beats__ —> __Elasticsearch__ —> __Kibana__ scenario.

To deploy Logstash to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/services/logstash-svc.yml
```

Then run:

```
     > kubectl apply -f ./k8s/configmaps/logstash-configmap.yml
```

And then run:

```
     > kubectl apply -f ./k8s/deployments/logstash-deployment.yml
```

To check the status, run:

```
     > kubectl get deployment/logstash-deployment -n kube-elk
```

You should see the following output:

```
      NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
      logstash-deployment   1/1     1            1           26m
```

Make sure that logstash container is up and running by viewing pod's _logstash_ container logs:

```
     > kubectl logs <logstash pod name> -c logstash -n kube-elk -f
```

Logstash also provides [monitoring APIs](https://www.elastic.co/guide/en/logstash/current/monitoring-logstash.html) for
retrieving runtime metrics about Logstash. By default, the monitoring API attempts to bind to port _tcp:9600_. So, to
access the Logstash monitoring API, we have to forward a local port _9600_ to the Kubernetes node running Logstash with
the following command:

```
     > kubectl port-forward <logstash pod name> 9600:9600 -n kube-elk
```

You should see the following output:

```
      Forwarding from 127.0.0.1:9600 -> 9600
      Forwarding from [::1]:9600 -> 9600
```

Now You can use the root resource to retrieve general information about the Logstash instance, including the host and
version with the following command:

```
     > curl localhost:9600/?pretty
```

You should see the following output:

```
      {
        "host" : "logstash",
        "version" : "6.8.23",
        "http_address" : "0.0.0.0:9600",
        "id" : "5db8766c-2737-47cc-80c6-26c3621604ec",
        "name" : "logstash",
        "build_date" : "2022-01-06T20:30:42Z",
        "build_sha" : "2d726680d98e4e6dfb093ff1a39cc1c0bf1d1ef5",
        "build_snapshot" : false
      }
```

#### 5. Deploying Kibana

_Kibana is a visualization tool. It uses a web browser interface to organize and display data._

To deploy Kibana to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/configmaps/kibana-configmap.yml
```

Then run:

```
     > kubectl apply -f ./k8s/services/kibana-svc.yml
```

And then run:

```
     > kubectl apply -f ./k8s/deployments/kibana-deployment.yml
```

To access the Kibana interface, we have to forward a local port _5601_ to the Kubernetes node running Kibana with the
following command:

```
     > kubectl port-forward <kibana pod> 5601:5601 -n kube-elk
```

The command forwards the connection and keeps it open. Leave the terminal window running and proceed to the next step.

To check the state of the deployment, in another terminal tab, perform the following request against the Elasticsearch
REST API:

```
     > curl localhost:9200/_cat/indices?v 
```

__Note:__ If you are running a single node cluster (Docker Desktop or MiniKube) you might need to perform the following
request against the Elasticsearch REST API::

```
     > curl --location --request PUT 'localhost:9200/_settings' \
       --header 'Content-Type: application/json' \
       --data '{
           "index": {
               "number_of_replicas": 0
           }
       }'
```

You should see the following output:

```
      health status index                      uuid                   pri rep docs.count docs.deleted store.size pri.store.size
      green  open   .kibana                    fP_HM1riQWGKpkl8FuGFTA   1   0          2            0     10.4kb         10.4kb
      green  open   .kibana_1                  g2SMz8XjShmSzTwmOQu9Fw   1   0          0            0       261b           261b
      green  open   .kibana_2                  2Poc2zmRRwawJNBO8Xeamg   1   0          0            0       261b           261b
      green  open   .kibana_task_manager       RgTFfA6lQ_CoSVUW8NbZGQ   1   0          2            0     19.2kb         19.2kb
      green  open   logstash-2023.05.27        sNFgElHBTbSbapgYPYk9Cw   5   0     132000            0     28.6mb         28.6mb
```

__Note:__ If you are running the __Beats__ —> __Logstash__ —> __Elasticsearch__ —> __Kibana__ scenario.

```
      health status index                      uuid                   pri rep docs.count docs.deleted store.size pri.store.size
      green  open   .kibana                    fP_HM1riQWGKpkl8FuGFTA   1   0          2            0     10.4kb         10.4kb
      green  open   .kibana_1                  g2SMz8XjShmSzTwmOQu9Fw   1   0          0            0       261b           261b
      green  open   .kibana_2                  2Poc2zmRRwawJNBO8Xeamg   1   0          0            0       261b           261b
      green  open   .kibana_task_manager       RgTFfA6lQ_CoSVUW8NbZGQ   1   0          2            0     19.2kb         19.2kb
      green  open   filebeat-6.8.23-2023.05.20 EUSLOZMWQGSyWMrh2EJiRA   5   0     122481            0     34.2mb         34.2mb
```

__Note:__ If you are running the __Beats__ —> __Elasticsearch__ —> __Kibana__ scenario.

And then access the Kibana UI in any browser:

```
     > http://localhost:5601
```

In Kibana, navigate to the __Management__ -> __Kibana Index Patterns__. Kibana should display the Filebeat index.

Enter __"logstash-*"__ or __"filebeat-*"__ (depending on running ELK pattern) as the index pattern, and in the next step
select @timestamp as your Time Filter field.

Navigate to the Kibana dashboard and in the __Discovery__ page, in the search bar enter:

```
     > kubernetes.pod_name:<name of the pod>
```

You should see a list of log entries for the specified pod.

#### 5. Deploying CronJob

_Elasticsearch cron job is used for clearing Elasticsearch indices._

To deploy Elasticsearch cron job to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/configmaps/curator-configmap.yml
```

And then run:

```
     > kubectl apply -f ./k8s/cronjobs/curator-cronjob.yml
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

### MariaDB database on K8S cluster

MariaDB Server is one of the most popular open source relational databases.

#### 1. Creating namespace for MariaDB database

To create a _kube-db_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-db-ns.yml
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
      kube-db                Active   99m     kubernetes.io/metadata.name=kube-db,name=kube-db
      kube-elk               Active   2d12h   kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-node-lease        Active   2d13h   kubernetes.io/metadata.name=kube-node-lease
      kube-public            Active   2d13h   kubernetes.io/metadata.name=kube-public
      kube-system            Active   2d13h   kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   2d13h   addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying MariaDB cluster

To deploy MariaDB cluster to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/rbacs/mariadb-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/configmaps/mariadb-configmap.yml
```

Then deploy a headless service for _MariaDB_ pods using the following command:

```
     > kubectl apply -f ./k8s/services/mariadb-svc.yml
```

__Note:__ You cannot directly access the application running in the pod. If you want to access the application, you need
a Service object in the Kubernetes cluster.

_Headless_ service means that only internal pods can communicate with each other. They are not exposed to external
requests outside the Kubernetes cluster. _Headless_ services expose the individual pod IPs instead of the service IP and
should be used when client applications or pods want to communicate with specific (not randomly selected) pod (stateful
application scenarios).

Get the list of running services under the __kube-db__ namespace with the following command:

```
     > kubectl get service -n kube-db
```

You should see the following output:

```
      NAME          TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)    AGE
      mariadb-svc   ClusterIP   None         <none>        3306/TCP   113s
```

Then run:

```
     > kubectl apply -f ./k8s/secrets/mariadb-secret.yml
```

Now the secrets can be referenced in our statefulset. And then run:

```
     > kubectl apply -f ./k8s/sets/mariadb-statefulset.yml
```

To monitor the deployment status, run:

```
     > kubectl rollout status sts/mariadb-sts -n kube-db
```

You should see the following output:

```
      partitioned roll out complete: 3 new pods have been updated...
```

To check the pod status, run:

```
     > kubectl get pods -n kube-db
```

You should see the following output:

```
      NAME            READY   STATUS    RESTARTS   AGE
      mariadb-sts-0   1/1     Running   0          108s
      mariadb-sts-1   1/1     Running   0          105s
      mariadb-sts-2   1/1     Running   0          102s
```

#### 3. Testing MariaDB cluster replication

At this point, your MariaDB cluster is ready for work. Test it as follows:

Create data on first (primary) replica set member with these commands:

```
     > kubectl -n kube-db exec -it mariadb-sts-0 -- mariadb -uroot -p12345 
```

You should see the following output:

```
      Defaulted container "mariadb" out of: mariadb, init-mariadb (init)
      Welcome to the MariaDB monitor.  Commands end with ; or \g.
      Your MariaDB connection id is 6
      Server version: 10.11.3-MariaDB-1:10.11.3+maria~ubu2204-log mariadb.org binary distribution
      
      Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.
      
      Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
      
      MariaDB [(none)]> show databases;
      +--------------------+
      | Database           |
      +--------------------+
      | information_schema |
      | mysql              |
      | performance_schema |
      | primary_db         |
      | sys                |
      +--------------------+
      5 rows in set (0.001 sec)
      
      MariaDB [(none)]> use primary_db;
      Database changed
      MariaDB [primary_db]> create table my_table (t int); insert into my_table values (5),(15),(25);
      Query OK, 0 rows affected (0.041 sec)
      
      Query OK, 3 rows affected (0.007 sec)
      Records: 3  Duplicates: 0  Warnings: 0
      MariaDB [primary_db]> exit
      Bye
```

Check data on second (secondary) replica set member with these commands:

```
     > kubectl -n kube-db exec -it mariadb-sts-1 -- mariadb -uroot -p12345 
```

You should see the following output:

```
      Defaulted container "mariadb" out of: mariadb, init-mariadb (init)
      Welcome to the MariaDB monitor.  Commands end with ; or \g.
      Your MariaDB connection id is 6
      Server version: 10.11.3-MariaDB-1:10.11.3+maria~ubu2204 mariadb.org binary distribution
      
      Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.
      
      Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
      
      MariaDB [(none)]> show databases;
      +--------------------+
      | Database           |
      +--------------------+
      | information_schema |
      | mysql              |
      | performance_schema |
      | primary_db         |
      | sys                |
      +--------------------+
      5 rows in set (0.001 sec)
      
      MariaDB [(none)]> use primary_db;
      Database changed
      MariaDB [primary_db]> show tables;
      +----------------------+
      | Tables_in_primary_db |
      +----------------------+
      | my_table             |
      +----------------------+
      1 row in set (0.000 sec)
      MariaDB [primary_db]> select * from my_table;
      +------+
      | t    |
      +------+
      |    5 |
      |   15 |
      |   25 |
      +------+
      3 rows in set (0.000 sec)
```

Repeat the same steps for the third (secondary) replica set member by changing the name of the pod to _mariadb-sts-2_.

### MongoDB database on K8S cluster

MongoDB is a source-available cross-platform document-oriented database program.

#### 1. Creating namespace for MongoDB database

To create a _kube-nosql-db_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-nosql-db-ns.yml
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
      kube-db                Active   99m     kubernetes.io/metadata.name=kube-db,name=kube-db
      kube-elk               Active   2d12h   kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-node-lease        Active   2d13h   kubernetes.io/metadata.name=kube-node-lease
      kube-nosql-db          Active   3m5s    kubernetes.io/metadata.name=kube-nosql-db,name=kube-nosql-db
      kube-public            Active   2d13h   kubernetes.io/metadata.name=kube-public
      kube-system            Active   2d13h   kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   2d13h   addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying MongoDB cluster

To deploy MongoDB cluster to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/rbacs/mongodb-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/configmaps/mongodb-configmap.yml
```

Then deploy a headless service for _MongoDB_ pods using the following command:

```
     > kubectl apply -f ./k8s/services/mongodb-svc.yml
```

__Note:__ You cannot directly access the application running in the pod. If you want to access the application, you need
a Service object in the Kubernetes cluster.

_Headless_ service means that only internal pods can communicate with each other. They are not exposed to external
requests outside the Kubernetes cluster. _Headless_ services expose the individual pod IPs instead of the service IP and
expose the individual pod IPs instead of the service IP and should be used when client applications or pods want to
communicate with specific (not randomly selected) pod (stateful application scenarios).

Get the list of running services under the __kube-nosql-db__ namespace with the following command:

```
     > kubectl get service -n kube-nosql-db
```

You should see the following output:

```
      NAME          TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)     AGE
      mongodb-svc   ClusterIP   None         <none>        27017/TCP   2m36s
```

Then run:

```
     > kubectl apply -f ./k8s/secrets/mongodb-secret.yml
```

Now the secrets can be referenced in our statefulset. And then run:

```
     > kubectl apply -f ./k8s/sets/mongodb-statefulset.yml
```

To monitor the deployment status, run:

```
     > kubectl rollout status sts/mongodb-sts -n kube-nosql-db
```

You should see the following output:

```
      Waiting for 3 pods to be ready...
      Waiting for 2 pods to be ready...
      Waiting for 1 pods to be ready...
      partitioned roll out complete: 3 new pods have been updated...
```

To check the pod status, run:

```
     > kubectl get pods -n kube-nosql-db -o wide
```

You should see the following output:

```
      NAME            READY   STATUS    RESTARTS   AGE   IP            NODE       NOMINATED NODE   READINESS GATES
      mongodb-sts-0   2/2     Running   0          85s   10.244.0.8    minikube   <none>           <none>
      mongodb-sts-1   2/2     Running   0          62s   10.244.0.9    minikube   <none>           <none>
      mongodb-sts-2   2/2     Running   0          58s   10.244.0.10   minikube   <none>           <none>
```

#### 3. Setting up MongoDB replication

Connect to the first replica set member with this command:

```
     > kubectl -n kube-nosql-db exec -it mongodb-sts-0 -- mongo
```

You should see the following output:

```
      Defaulted container "mongodb" out of: mongodb, mongo-sidecar
      MongoDB shell version v4.4.21
      connecting to: mongodb://127.0.0.1:27017/?compressors=disabled&gssapiServiceName=mongodb
      Implicit session: session { "id" : UUID("c3a2b74c-75f0-4288-9deb-30a7d0bc4bd6") }
      MongoDB server version: 4.4.21
      ---
      The server generated these startup warnings when booting:
      2023-05-27T10:11:59.717+00:00: Using the XFS filesystem is strongly recommended with the WiredTiger storage engine. See http://dochub.mongodb.org/core/prodnotes-filesystem
      2023-05-27T10:12:00.959+00:00: Access control is not enabled for the database. Read and write access to data and configuration is unrestricted
      2023-05-27T10:12:00.960+00:00: You are running this process as the root user, which is not recommended
      ---
      ---
              Enable MongoDB's free cloud-based monitoring service, which will then receive and display
              metrics about your deployment (disk utilization, CPU, operation statistics, etc).
      
              The monitoring data will be available on a MongoDB website with a unique URL accessible to you
              and anyone you share the URL with. MongoDB may use this information to make product
              improvements and to suggest MongoDB products and deployment options to you.
      
              To enable free monitoring, run the following command: db.enableFreeMonitoring()
              To permanently disable this reminder, run the following command: db.disableFreeMonitoring()
      ---
```

You now have a REPL environment connected to the MongoDB database. Initiate the replication by executing the following
command:

```
     > rs.initiate()
```

If you get the following output:

```
      {
              "operationTime" : Timestamp(1685727395, 1),
              "ok" : 0,
              "errmsg" : "already initialized",
              "code" : 23,
              "codeName" : "AlreadyInitialized",
              "$clusterTime" : {
                      "clusterTime" : Timestamp(1685727395, 1),
                      "signature" : {
                              "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                              "keyId" : NumberLong(0)
                      }
              }
      }
```

Define the variable called __cfg__. The variable executes rs.conf() command:

```
     > cfg = rs.conf()
```

Use the __cfg__ variable to add the replica set members to the configuration:

```
      > cfg.members = [{_id: 0, host: "mongodb-sts-0.mongodb-svc.kube-nosql-db"},
                       {_id: 1, host: "mongodb-sts-1.mongodb-svc.kube-nosql-db", priority: 0},
                       {_id: 2, host: "mongodb-sts-2.mongodb-svc.kube-nosql-db", priority: 0}]
```

You should see the following output:

```
[
        {
                "_id" : 0,
                "host" : "mongodb-sts-0.mongodb-svc.kube-nosql-db"
        },
        {
                "_id" : 1,
                "host" : "mongodb-sts-1.mongodb-svc.kube-nosql-db",
                "priority": 0
        },
        {
                "_id" : 2,
                "host" : "mongodb-sts-2.mongodb-svc.kube-nosql-db",
                "priority": 0
        }
]
```

Confirm the configuration by executing the following command:

```
     > rs.reconfig(cfg, {force: true})
```

You should see the following output:

```
      {
        ok: 1,
        '$clusterTime': {
          clusterTime: Timestamp({ t: 1684949311, i: 1 }),
          signature: {
            hash: Binary(Buffer.from("0000000000000000000000000000000000000000", "hex"), 0),
            keyId: Long("0")
          }
        },
        operationTime: Timestamp({ t: 1684949311, i: 1 })
      }
```

Verify MongoDB replication status with this command:

```
     > rs.status()
```

You should see the following output:

```
      {
              "set" : "rs0",
              "date" : ISODate("2023-05-27T10:14:52.096Z"),
              "myState" : 1,
              "term" : NumberLong(1),
              "syncSourceHost" : "",
              "syncSourceId" : -1,
              "heartbeatIntervalMillis" : NumberLong(2000),
              "majorityVoteCount" : 2,
              "writeMajorityCount" : 2,
              "votingMembersCount" : 3,
              "writableVotingMembersCount" : 3,
              "optimes" : {
                      "lastCommittedOpTime" : {
                              "ts" : Timestamp(1685182483, 1),
                              "t" : NumberLong(1)
                      },
                      "lastCommittedWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                      "readConcernMajorityOpTime" : {
                              "ts" : Timestamp(1685182483, 1),
                              "t" : NumberLong(1)
                      },
                      "readConcernMajorityWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                      "appliedOpTime" : {
                              "ts" : Timestamp(1685182483, 1),
                              "t" : NumberLong(1)
                      },
                      "durableOpTime" : {
                              "ts" : Timestamp(1685182483, 1),
                              "t" : NumberLong(1)
                      },
                      "lastAppliedWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                      "lastDurableWallTime" : ISODate("2023-05-27T10:14:43.714Z")
              },
              "lastStableRecoveryTimestamp" : Timestamp(1685182438, 1),
              "electionCandidateMetrics" : {
                      "lastElectionReason" : "electionTimeout",
                      "lastElectionDate" : ISODate("2023-05-27T10:12:03.578Z"),
                      "electionTerm" : NumberLong(1),
                      "lastCommittedOpTimeAtElection" : {
                              "ts" : Timestamp(0, 0),
                              "t" : NumberLong(-1)
                      },
                      "lastSeenOpTimeAtElection" : {
                              "ts" : Timestamp(1685182323, 1),
                              "t" : NumberLong(-1)
                      },
                      "numVotesNeeded" : 1,
                      "priorityAtElection" : 1,
                      "electionTimeoutMillis" : NumberLong(10000),
                      "newTermStartDate" : ISODate("2023-05-27T10:12:03.670Z"),
                      "wMajorityWriteAvailabilityDate" : ISODate("2023-05-27T10:12:03.712Z")
              },
              "members" : [
                      {
                              "_id" : 0,
                              "name" : "10.244.1.83:27017",
                              "health" : 1,
                              "state" : 1,
                              "stateStr" : "PRIMARY",
                              "uptime" : 173,
                              "optime" : {
                                      "ts" : Timestamp(1685182483, 1),
                                      "t" : NumberLong(1)
                              },
                              "optimeDate" : ISODate("2023-05-27T10:14:43Z"),
                              "lastAppliedWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "lastDurableWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "syncSourceHost" : "",
                              "syncSourceId" : -1,
                              "infoMessage" : "",
                              "electionTime" : Timestamp(1685182323, 2),
                              "electionDate" : ISODate("2023-05-27T10:12:03Z"),
                              "configVersion" : 5,
                              "configTerm" : 1,
                              "self" : true,
                              "lastHeartbeatMessage" : ""
                      },
                      {
                              "_id" : 1,
                              "name" : "mongodb-sts-1.mongodb-svc.kube-nosql-db:27017",
                              "health" : 1,
                              "state" : 2,
                              "stateStr" : "SECONDARY",
                              "uptime" : 53,
                              "optime" : {
                                      "ts" : Timestamp(1685182483, 1),
                                      "t" : NumberLong(1)
                              },
                              "optimeDurable" : {
                                      "ts" : Timestamp(1685182483, 1),
                                      "t" : NumberLong(1)
                              },
                              "optimeDate" : ISODate("2023-05-27T10:14:43Z"),
                              "optimeDurableDate" : ISODate("2023-05-27T10:14:43Z"),
                              "lastAppliedWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "lastDurableWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "lastHeartbeat" : ISODate("2023-05-27T10:14:51.822Z"),
                              "lastHeartbeatRecv" : ISODate("2023-05-27T10:14:51.853Z"),
                              "pingMs" : NumberLong(0),
                              "lastHeartbeatMessage" : "",
                              "syncSourceHost" : "10.244.1.83:27017",
                              "syncSourceId" : 0,
                              "infoMessage" : "",
                              "configVersion" : 5,
                              "configTerm" : 1
                      },
                      {
                              "_id" : 2,
                              "name" : "mongodb-sts-2.mongodb-svc.kube-nosql-db:27017",
                              "health" : 1,
                              "state" : 2,
                              "stateStr" : "SECONDARY",
                              "uptime" : 34,
                              "optime" : {
                                      "ts" : Timestamp(1685182483, 1),
                                      "t" : NumberLong(1)
                              },
                              "optimeDurable" : {
                                      "ts" : Timestamp(1685182483, 1),
                                      "t" : NumberLong(1)
                              },
                              "optimeDate" : ISODate("2023-05-27T10:14:43Z"),
                              "optimeDurableDate" : ISODate("2023-05-27T10:14:43Z"),
                              "lastAppliedWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "lastDurableWallTime" : ISODate("2023-05-27T10:14:43.714Z"),
                              "lastHeartbeat" : ISODate("2023-05-27T10:14:51.823Z"),
                              "lastHeartbeatRecv" : ISODate("2023-05-27T10:14:50.251Z"),
                              "pingMs" : NumberLong(0),
                              "lastHeartbeatMessage" : "",
                              "syncSourceHost" : "mongodb-sts-1.mongodb-svc.kube-nosql-db:27017",
                              "syncSourceId" : 1,
                              "infoMessage" : "",
                              "configVersion" : 5,
                              "configTerm" : 1
                      }
              ],
              "ok" : 1,
              "$clusterTime" : {
                      "clusterTime" : Timestamp(1685182483, 1),
                      "signature" : {
                              "hash" : BinData(0,"AAAAAAAAAAAAAAAAAAAAAAAAAAA="),
                              "keyId" : NumberLong(0)
                      }
              },
              "operationTime" : Timestamp(1685182483, 1)
      }
```

__Note:__ The _members_ section of the status output shows three replicas. The pod mongodb-sts-0 is listed as the __
Primary__ replica, while the other two pods, _mongodb-sts-1_ and _mongodb-sts-2_, are listed as the __Secondary__
replicas.

The ReplicaSet deployment of MongoDB is set up and ready to operate.

Quit the replica set member with the following command:

```
     > exit
```

#### 4. Setting up MongoDB admin credentials

Now let's create the admin account.

Connect to the first (primary) replica set member shell with the following command:

```
     > kubectl -n kube-nosql-db exec -it mongodb-sts-0 -- mongo
```

Switch to admin database with the following command:

```
     > use admin
```

Create admin user with the following command:

```
     > db.createUser({ user:'admin', pwd:'mongo12345', roles:[ { role:'userAdminAnyDatabase', db: 'admin'}]})
```

You should see the following output:

```
Successfully added user: {
        "user" : "admin",
        "roles" : [
                {
                        "role" : "userAdminAnyDatabase",
                        "db" : "admin"
                }
        ]
}
```

It means admin account has been created successfully. Quit the replica set member with the following command:

```
     > exit
```

#### 5. Testing MongoDB cluster replication

At this point, your MongoDB cluster is ready for work. Test it as follows:

Connect to the first (primary) replica set member shell with the following command:

```
     > kubectl -n kube-nosql-db exec -it mongodb-sts-0 -- mongo
```

Display all databases with the following command:

```
     > show dbs
```

You should see the following output:

```
      admin    80.00 KiB
      config  176.00 KiB
      local   404.00 KiB
```

Switch to the __test__ database (if not) and add test entries with the following commands:

```
     > use test
     > db.games.insertOne({name: "RPS game", language: "Java" })
     > db.games.insertOne({name: "Tic-Tac-Toe game" })
```

Display all data from the test database with the following commands:

```
     > db.games.find()
```

You should see the following output:

```
      { "_id" : ObjectId("6471d9141175a02c9a9c27a0"), "name" : "RPS game", "language" : "Java" }
      { "_id" : ObjectId("6471d9211175a02c9a9c27a1"), "name" : "Tic-Tac-Toe game" }
```

Quit the primary replica set member with the following command:

```
     > exit
```

Connect to the second (secondary) replica set member shell with the following command:

```
     > kubectl -n kube-nosql-db exec -it mongodb-sts-1 -- mongo
```

Set a read preference to the secondary replica set member with the following command:

```
     > rs.secondaryOk()
```

Display all databases with the following command:

```
     > show dbs
```

You should see the following output:

```
      admin    80.00 KiB
      config  176.00 KiB
      local   404.00 KiB
      test     72.00 KiB
```

Display all data from the test database with the following commands:

```
     > db.games.find()
```

You should see the following output:

```
      { "_id" : ObjectId("6471d9141175a02c9a9c27a0"), "name" : "RPS game", "language" : "Java" }
      { "_id" : ObjectId("6471d9211175a02c9a9c27a1"), "name" : "Tic-Tac-Toe game" }
```

Repeat the same steps for the third (secondary) replica set member by changing the name of the pod to _mongodb-sts-2_.

### Mongo Express web-based MongoDB admin application on K8S cluster

[Mongo Express](https://github.com/mongo-express/mongo-express) is an open source, basic web-based MongoDB admin
interface.

#### 1. Deploying Simple Single Service Ingress for Mongo Express application

To create a [Simple Single Service Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) for the
Mongo Express application, run:

```
     > kubectl apply -f ./k8s/ingress/mongodb-ingress.yml
```

__Note:__ A Mongo Express
application [Simple Single Service Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress)
configuration exposes only one service to external users.

![Simple Single Service Ingress](https://d33wubrfki0l68.cloudfront.net/91ace4ec5dd0260386e71960638243cf902f8206/c3c52/docs/images/ingress.svg)

Make sure the Mongo Express application ingress has been created:

```
     > kubectl get ingress -n kube-nosql-db
```

You should see the following output:

```
      NAME                    CLASS   HOSTS                    ADDRESS        PORTS   AGE
      mongodb-ingress         nginx   mongodb.internal         192.168.49.2   80      40h
```

Note the ip address (192.168.49.2) displayed in the output, as you will need this in the next step.

#### 2. Adding custom entry to the etc/host file for the Mongo Express application

Add a custom entry to the etc/hosts file using the nano text editor:

```
     > sudo nano /etc/hosts
```

You should add the following ip address (copied in the previous step) and custom domain to the hosts file:

```
      192.168.49.2 mongodb.internal
```

You may check the custom domain name with ping command:

```
     > ping mongodb.internal
```

You should see the following output:

```
      64 bytes from mongodb.internal (192.168.49.2): icmp_seq=1 ttl=64 time=0.072 ms
      64 bytes from mongodb.internal (192.168.49.2): icmp_seq=2 ttl=64 time=0.094 ms
      64 bytes from mongodb.internal (192.168.49.2): icmp_seq=3 ttl=64 time=0.042 ms
```

Access the Mongo Express application from any browser by typing:

```
      > mongodb.internal
```

#### 3. Deploying Mongo Express application

To deploy Mongo Express to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/services/mongodb-express-svc.yml
```

It deploys a ClusterIP service for _Mongo Express_ pods.

Then run:

```
     > kubectl apply -f ./k8s/deployment/mongodb-express-deployment.yml
```

### Redis database on K8S cluster

_Redis_ is an open source, in-memory data structure store used as a distributed cache in the RPS application. It is used
to store data in a key-value format, allowing for fast access and retrieval of data. Redis is a popular choice for
distributed caching due to its scalability, performance, and flexibility.

#### 1. Creating namespace for Redis database

To create a _kube-cache_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-cache-ns.yml
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
      kube-cache             Active   25s     kubernetes.io/metadata.name=kube-cache,name=kube-cache
      kube-db                Active   99m     kubernetes.io/metadata.name=kube-db,name=kube-db
      kube-elk               Active   2d12h   kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-node-lease        Active   2d13h   kubernetes.io/metadata.name=kube-node-lease
      kube-public            Active   2d13h   kubernetes.io/metadata.name=kube-public
      kube-system            Active   2d13h   kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   2d13h   addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying Redis cluster

To deploy _Redis_ cluster to Kubernetes, first run:

```
     > kubectl apply -f ./k8s/rbacs/redis-rbac.yml
```

Then run:

```
     > kubectl apply -f ./k8s/configmaps/redis-configmap.yml
```

Then deploy a headless service for _Redis_ pods using the following command:

```
     > kubectl apply -f ./k8s/services/redis-svc.yml
```

__Note:__ You cannot directly access the application running in the pod. If you want to access the application, you need
a Service object in the Kubernetes cluster.

_Headless_ service means that only internal pods can communicate with each other. They are not exposed to external
requests outside the Kubernetes cluster. _Headless_ services expose the individual pod IPs instead of the service IP and
should be used when client applications or pods want to communicate with specific (not randomly selected) pod (stateful
application scenarios).

To get the list of running services under the _Redis_ namespace, run:

```
     > kubectl get service -n kube-cache
```

You should see the following output:

```
      NAME        TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)    AGE
      redis-svc   ClusterIP   None         <none>        6379/TCP   4h11m
```

Then run:

```
     > kubectl apply -f ./k8s/secrets/redis-secret.yml
```

Now the secrets can be referenced in our statefulset. And then run:

```
     > kubectl apply -f ./k8s/sets/redis-statefulset.yml
```

To monitor the deployment status, run:

```
     > kubectl rollout status sts/redis-sts -n kube-cache
```

You should see the following output:

```
      statefulset rolling update complete 3 pods at revision redis-sts-85577d848c...
```

To check the pod status, run:

```
     > kubectl get pods -n kube-cache
```

You should see the following output:

```
      NAME          READY   STATUS    RESTARTS   AGE
      redis-sts-0   1/1     Running   0          5m40s
      redis-sts-1   1/1     Running   0          5m37s
      redis-sts-2   1/1     Running   0          5m34s
```

#### 3. Testing Redis cluster replication

At this point, your Redis cluster is ready for work. Test it as follows:

Connect to the first (master) replica set member shell with the following command:

```
     > kubectl -n kube-cache exec -it redis-sts-0 -- sh
```

Then connect to Redis the Redis CLI:

```
     # redis-cli
```

You should see the following output:

```
      127.0.0.1:6379>
```

Authenticate to Redis with following command:

```
      127.0.0.1:6379> auth 12345
```

Check the replica member replication information with the following command:

```
     127.0.0.1:6379> info replication
```

You should see the following output:

```
      # Replication
      role:master
      connected_slaves:2
      slave0:ip=10.244.1.205,port=6379,state=online,offset=952,lag=1
      slave1:ip=10.244.1.206,port=6379,state=online,offset=952,lag=1
      master_replid:e7add4a40b5434360c75163ab01d8871928c5f03
      master_replid2:0000000000000000000000000000000000000000
      master_repl_offset:952
      second_repl_offset:-1
      repl_backlog_active:1
      repl_backlog_size:1048576
      repl_backlog_first_byte_offset:1
      repl_backlog_histlen:952
```

Check the roles of the replica member with the following command:

```
     127.0.0.1:6379> role
```

You should see the following output:

```
      1) "master"
      2) (integer) 728
      3) 1) 1) "10.244.1.205"
            2) "6379"
            3) "728"
         2) 1) "10.244.1.206"
            2) "6379"
            3) "728"
```

Create some key-value pair data using the following command:

```
     127.0.0.1:6379> set game1 RPS
     OK
     127.0.0.1:6379> set game2 Tic-Tac-Toe
     OK
```

Now get the key-value pair list with the following command:

```
     127.0.0.1:6379> keys *
```

You should see the following output:

```
      1) "game1"
      2) "game2"
```

Connect to the second (slave) replica set member shell with the following command:

```
     > kubectl -n kube-cache exec -it redis-sts-1 -- sh
```

Then connect to Redis the Redis CLI:

```
     # redis-cli
```

And type the following command:

```
      127.0.0.1:6379> keys *
```

You should see the following output:

```
      1) "game1"
      2) "game2"
```

Repeat the same steps for the third (slave) replica set member by changing the name of the pod to _redis-sts-2_.

### Prometheus, Alertmanager and Grafana (Monitoring Stack) on K8S cluster

_Monitoring Stack_ is an open-source [Prometheus](https://prometheus.io)
, [Alertmanager](https://prometheus.io/docs/alerting/latest/alertmanager) and Grafana monitoring infrastructure in
Kubernetes.

There are three necessary services in _Monitoring Stack_ setup:

[Prometheus](https://prometheus.io) endpoint(s) is the application with metrics that we want to track.

[Prometheus](https://prometheus.io) is a monitoring system and time-series database.

[Alertmanager](https://prometheus.io/docs/alerting/latest/alertmanager) handles alerts sent by Prometheus server.

[Grafana](https://grafana.com) is a visualization tool that can use [Prometheus](https://prometheus.io) to create
dashboards and graphs.

#### 1. Creating namespace for Monitoring Stack

To create a _kube-monitoring_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-monitoring-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

You should see the following output:

```
      NAME                   STATUS   AGE     LABELS
      default                Active   10d     kubernetes.io/metadata.name=default
      ingress-nginx          Active   10d     app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx,kubernetes.io/metadata.name=ingress-nginx
      kube-cache             Active   3d16h   kubernetes.io/metadata.name=kube-cache,name=kube-cache
      kube-db                Active   6d19h   kubernetes.io/metadata.name=kube-db,name=kube-db
      kube-elk               Active   18h     kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-monitoring        Active   29m     kubernetes.io/metadata.name=kube-monitoring,name=kube-monitoring
      kube-node-lease        Active   10d     kubernetes.io/metadata.name=kube-node-lease
      kube-nosql-db          Active   26h     kubernetes.io/metadata.name=kube-nosql-db,name=kube-nosql-db
      kube-public            Active   10d     kubernetes.io/metadata.name=kube-public
      kube-system            Active   10d     kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   10d     addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying Monitoring cluster

We used to manually deploy Kubernetes manifest files. Making changes to K8S files as required making the process lengthy
and prone to errors as there's no consistency of deployments with this approach. With a fresh Kubernetes cluster, you
need to define the namespace, create storage classes, and then deploy your application to the cluster. The process is
quite lengthy, and if something goes wrong, it becomes a tedious process to find the problem.

[Helm](https://helm.sh) is a package manager for Kubernetes that allows us to easily install and manage applications on
Kubernetes clusters.

I am going to use [Helm](https://helm.sh) to deploy the _Monitoring Stack_ to the cluster. _Monitoring Stack_ comes with
a bunch of standard and third party Kubernetes components. [Helm](https://helm.sh) allows you to deploy the _Monitoring
Stack_ currently without having strong working knowledge of Kubernetes.

To deploy _Monitoring Stack_ to Kubernetes cluster with [helm charts](https://helm.sh/docs/topics/charts), just run:

```
     > helm install prometheus prometheus-community/kube-prometheus-stack -n kube-monitoring
```

Wait for some time until the chart is deployed. You should see the following output:

```
      NAME: prometheus
      LAST DEPLOYED: Sun May 28 12:12:25 2023
      NAMESPACE: kube-monitoring
      STATUS: deployed
      REVISION: 1
      NOTES:
      kube-prometheus-stack has been installed. Check its status by running:
        kubectl --namespace kube-monitoring get pods -l "release=prometheus"
      
      Visit https://github.com/prometheus-operator/kube-prometheus for instructions on how to create & configure Alertmanager and Prometheus instances using the Operator.
```

That's it! To check the pod status, run:

```
     > kubectl get all -n kube-monitoring
```

You should see all installed _Monitoring Stack__ components.

To access the [Prometheus](https://prometheus.io) locally, we have to forward a local port 9090 to the Kubernetes node
running [Prometheus](https://prometheus.io)
with the following command:

```
     > kubectl port-forward <prometheus pod name> 9090:9090 -n kube-monitoring
```

You should see the following output:

```
      Forwarding from 127.0.0.1:9090 -> 9090
      Forwarding from [::1]:9090 -> 9090
```

Now you can access the dashboard in the browser on http://localhost:9090.

[Prometheus HTTP API](https://prometheus.io/docs/prometheus/latest/querying/api)

### Grafana web-based application on K8S cluster

[Grafana](https://grafana.com) is a multi-platform open source analytics and interactive visualization web application.

#### 1. Deploying Simple Single Service Ingress for Grafana application

If you open the grafana service using the following command:

```
     > kubectl get service prometheus-grafana -n kube-monitoring -o yaml
```

you will see that the default port for Grafana dashboard is 3000:

```
        ports:
        - name: http-web
          port: 80
          protocol: TCP
          targetPort: 3000
```

So, we can forward port to host by ingress component and thus gain access to the dashboard by browser.

To create a [Simple Single Service Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) for the
Grafana application, run:

```
     > kubectl apply -f ./k8s/ingress/grafana-ingress.yml
```

__Note:__ A Grafana
application [Simple Single Service Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress)
configuration exposes only one service to external users.

![Simple Single Service Ingress](https://d33wubrfki0l68.cloudfront.net/91ace4ec5dd0260386e71960638243cf902f8206/c3c52/docs/images/ingress.svg)

Make sure the Grafana application ingress has been created:

```
     > kubectl get ingress -n kube-monitoring
```

You should see the following output:

```
      NAME                    CLASS   HOSTS                    ADDRESS        PORTS   AGE
      grafana-ingress         nginx   grafana.internal         192.168.49.2   80      25s
```

Note the ip address (192.168.49.2) displayed in the output, as you will need this in the next step.

#### 2. Adding custom entry to the etc/host file for the Grafana application

Add a custom entry to the etc/hosts file using the nano text editor:

```
     > sudo nano /etc/hosts
```

You should add the following ip address (copied in the previous step) and custom domain to the hosts file:

```
      192.168.49.2  grafana.internal mongodb.internal
```

You may check the custom domain name with ping command:

```
     > ping grafana.internal
```

You should see the following output:

```
      64 bytes from grafana.internal (192.168.49.2): icmp_seq=1 ttl=64 time=0.072 ms
      64 bytes from grafana.internal (192.168.49.2): icmp_seq=2 ttl=64 time=0.094 ms
      64 bytes from grafana.internal (192.168.49.2): icmp_seq=3 ttl=64 time=0.042 ms
```

Access the Grafana application from any browser by typing:

```
      > grafana.internal
```

### Apache Kafka on K8S cluster

[Apache Kafka](https://kafka.apache.org) is an open-source, event streaming platform that is distributed, scalable,
high-throughput, low-latency, and has a very large ecosystem.

#### 1. Creating namespace for Kafka

To create a _kube-kafka_ namespace on the k8s cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/kube-kafka-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

You should see the following output:

```
      NAME                   STATUS   AGE     LABELS
      default                Active   10d     kubernetes.io/metadata.name=default
      ingress-nginx          Active   10d     app.kubernetes.io/instance=ingress-nginx,app.kubernetes.io/name=ingress-nginx,kubernetes.io/metadata.name=ingress-nginx
      kube-cache             Active   3d16h   kubernetes.io/metadata.name=kube-cache,name=kube-cache
      kube-db                Active   6d19h   kubernetes.io/metadata.name=kube-db,name=kube-db
      kube-elk               Active   18h     kubernetes.io/metadata.name=kube-elk,name=kube-elk
      kube-kafka             Active   23s     kubernetes.io/metadata.name=kube-kafka,name=kube-kafka
      kube-monitoring        Active   29m     kubernetes.io/metadata.name=kube-monitoring,name=kube-monitoring
      kube-node-lease        Active   10d     kubernetes.io/metadata.name=kube-node-lease
      kube-nosql-db          Active   26h     kubernetes.io/metadata.name=kube-nosql-db,name=kube-nosql-db
      kube-public            Active   10d     kubernetes.io/metadata.name=kube-public
      kube-system            Active   10d     kubernetes.io/metadata.name=kube-system
      kubernetes-dashboard   Active   10d     addonmanager.kubernetes.io/mode=Reconcile,kubernetes.io/metadata.name=kubernetes-dashboard,kubernetes.io/minikube-addons=dashboard
```

#### 2. Deploying Apache Zookeeper cluster

The first step is to deploy [Apache Zookeeper](https://zookeeper.apache.org) on your K8S cluster
using [Zookeeper Bitnami's Helm chart](https://github.com/bitnami/charts/tree/main/bitnami/zookeeper).

The [Apache Zookeeper](https://zookeeper.apache.org) deployment will use
this [Apache Zookeeper](https://zookeeper.apache.org) deployment for coordination and management.

First, add the [Bitnami charts repository](https://github.com/bitnami/charts/tree/main/bitnami/zookeeper) to Helm:

```
     > helm repo add bitnami https://charts.bitnami.com/bitnami
```

You should see the following output:

```
      "bitnami" has been added to your repositories
```

Then execute the following command to deploy an [Apache Zookeeper](https://zookeeper.apache.org) cluster with 3 nodes:

```
     > helm install zookeeper bitnami/zookeeper --set image.tag=3.8.0-debian-10-r78 --set replicaCount=3 --set auth.enabled=false --set allowAnonymousLogin=true -n kube-kafka
```

Wait for some time until the chart is deployed. You should see the following output:

```
      NAME: zookeeper
      LAST DEPLOYED: Wed May 31 19:50:42 2023
      NAMESPACE: kube-kafka
      STATUS: deployed
      REVISION: 1
      TEST SUITE: None
      NOTES:
      CHART NAME: zookeeper
      CHART VERSION: 11.4.2
      APP VERSION: 3.8.1
      
      ** Please be patient while the chart is being deployed **
      
      ZooKeeper can be accessed via port 2181 on the following DNS name from within your cluster:
      
          zookeeper.kube-kafka.svc.cluster.local
      
      To connect to your ZooKeeper server run the following commands:
      
          export POD_NAME=$(kubectl get pods --namespace kube-kafka -l "app.kubernetes.io/name=zookeeper,app.kubernetes.io/instance=zookeeper,app.kubernetes.io/component=zookeeper" -o jsonpath="{.items[0].metadata.name}")
          kubectl exec -it $POD_NAME -- zkCli.sh
      
      To connect to your ZooKeeper server from outside the cluster execute the following commands:
      
          kubectl port-forward --namespace kube-kafka svc/zookeeper 2181:2181 &
          zkCli.sh 127.0.0.1:2181
```

Note the service name displayed in the output, as you will need this in subsequent steps.

```
      zookeeper.kube-kafka.svc.cluster.local
```

Make sure that the Zookeeper cluster is up and running with the following command:

```
     > kubectl get pods -n kube-kafka -o wide
```

You should see the following output:

```
      NAME              READY   STATUS    RESTARTS   AGE   IP            NODE       NOMINATED NODE   READINESS GATES
      zookeeper-0       1/1     Running   0          81s   10.244.0.22   minikube   <none>           <none>
      zookeeper-1       1/1     Running   0          81s   10.244.0.24   minikube   <none>           <none>
      zookeeper-2       1/1     Running   0          81s   10.244.0.23   minikube   <none>           <none>
```

#### 3. Deploying Apache Kafka cluster

The next step is to deploy [Apache Zookeeper](https://zookeeper.apache.org), again
with [Kafka Bitnami's Helm chart](https://github.com/bitnami/charts/tree/main/bitnami/kafka). In this case, we will
provide the name of the [Apache Zookeeper](https://zookeeper.apache.org) service as a parameter to the Helm chart.

```
     > helm install kafka bitnami/kafka --set image.tag=2.7.0-debian-10-r100 --set zookeeper.enabled=false --set kraft.enabled=false --set replicaCount=3 --set externalZookeeper.servers=zookeeper.kube-kafka -n kube-kafka
```

This command will deploy a three-node [Apache Zookeeper](https://zookeeper.apache.org) cluster and configure the nodes
to connect to the [Apache Zookeeper](https://zookeeper.apache.org)
service. Wait for some time until the chart is deployed. You should see the following output:

```
      NAME: kafka
      LAST DEPLOYED: Wed May 31 19:53:02 2023
      NAMESPACE: kube-kafka
      STATUS: deployed
      REVISION: 1
      TEST SUITE: None
      NOTES:
      CHART NAME: kafka
      CHART VERSION: 22.1.3
      APP VERSION: 3.4.0
      
      ** Please be patient while the chart is being deployed **
      
      Kafka can be accessed by consumers via port 9092 on the following DNS name from within your cluster:
      
          kafka.kube-kafka.svc.cluster.local
      
      Each Kafka broker can be accessed by producers via port 9092 on the following DNS name(s) from within your cluster:
      
          kafka-0.kafka-headless.kube-kafka.svc.cluster.local:9092
          kafka-1.kafka-headless.kube-kafka.svc.cluster.local:9092
          kafka-2.kafka-headless.kube-kafka.svc.cluster.local:9092
      
      To create a pod that you can use as a Kafka client run the following commands:
      
          kubectl run kafka-client --restart='Never' --image docker.io/bitnami/kafka:2.7.0-debian-10-r100 --namespace kube-kafka --command -- sleep infinity
          kubectl exec --tty -i kafka-client --namespace kube-kafka -- bash
      
          PRODUCER:
              kafka-console-producer.sh \
                  --broker-list kafka-0.kafka-headless.kube-kafka.svc.cluster.local:9092,kafka-1.kafka-headless.kube-kafka.svc.cluster.local:9092,kafka-2.kafka-headless.kube-kafka.svc.cluster.local:9092 \
                  --topic test
      
          CONSUMER:
              kafka-console-consumer.sh \
                  --bootstrap-server kafka.kube-kafka.svc.cluster.local:9092 \
                  --topic test \
                  --from-beginning
```

Note the service name displayed in the output, as you will need this in the next step:

```
      kafka.kube-kafka.svc.cluster.local
```

Also note the Kafka broker access details, as you will need this for microservice Kafka configurations (ConfigMap of
each microservice):

```
      kafka-0.kafka-sts-headless.kube-kafka.svc.cluster.local:9092
      kafka-1.kafka-sts-headless.kube-kafka.svc.cluster.local:9092
      kafka-2.kafka-sts-headless.kube-kafka.svc.cluster.local:9092
```

Make sure that the Kafka cluster is up and running with the following command:

```
     > kubectl get pods -n kube-kafka -o wide
```

You should see the following output:

```
      NAME          READY   STATUS    RESTARTS   AGE     IP            NODE       NOMINATED NODE   READINESS GATES
      kafka-0       1/1     Running   0          70s     10.244.0.36   minikube   <none>           <none>
      kafka-1       1/1     Running   0          70s     10.244.0.35   minikube   <none>           <none>
      kafka-2       1/1     Running   0          70s     10.244.0.34   minikube   <none>           <none>
      zookeeper-0   1/1     Running   0          3m31s   10.244.0.32   minikube   <none>           <none>
      zookeeper-1   1/1     Running   0          3m31s   10.244.0.31   minikube   <none>           <none>
      zookeeper-2   1/1     Running   0          3m31s   10.244.0.33   minikube   <none>           <none>
```

Check the kafka logs with the following command:

```
      > kubectl logs kafka-0 -n kube-kafka -f
```

To confirm that the [Apache Zookeeper](https://zookeeper.apache.org)
and [Apache Zookeeper](https://zookeeper.apache.org) deployments are connected, check the logs for any of the Apache
Kafka pods and ensure that you see lines similar to the ones shown below, which confirm the connection:

```
      [2023-05-31 19:53:10,838] INFO Socket connection established, initiating session, client: /10.244.0.36:47092, server: zookeeper.kube-kafka/10.110.153.100:2181 (org.apache.zookeeper.ClientCnxn)
      [2023-05-31 19:53:10,849] INFO Session establishment complete on server zookeeper.kube-kafka/10.110.153.100:2181, sessionid = 0x30000c058cd0001, negotiated timeout = 18000 (org.apache.zookeeper.ClientCnxn)
      [2023-05-31 19:53:10,854] INFO [ZooKeeperClient Kafka server] Connected. (kafka.zookeeper.ZooKeeperClient)
      [2023-05-31 19:53:10,978] INFO [feature-zk-node-event-process-thread]: Starting (kafka.server.FinalizedFeatureChangeListener$ChangeNotificationProcessorThread)
      [2023-05-31 19:53:11,014] INFO Feature ZK node at path: /feature does not exist (kafka.server.FinalizedFeatureChangeListener)
      [2023-05-31 19:53:11,020] INFO Cleared cache (kafka.server.FinalizedFeatureCache)
      [2023-05-31 19:53:11,246] INFO Cluster ID = mq2vGCG7RSOJX0vsqWFS9A (kafka.server.KafkaServer)
      [2023-05-31 19:53:11,265] WARN No meta.properties file under dir /bitnami/kafka/data/meta.properties (kafka.server.BrokerMetadataCheckpoint)
```

#### 4. Testing Apache Kafka cluster

At this point, your [Apache Zookeeper](https://zookeeper.apache.org) cluster is ready for work. Test it as follows:

Create a topic named __mytopic__ using the commands below. Replace the _ZOOKEEPER-SERVICE-NAME_ placeholder with the
[Apache Zookeeper](https://zookeeper.apache.org) service name obtained earlier:

```
      > kubectl --namespace kube-kafka exec -it <name of kafka pod> -- kafka-topics.sh --create --zookeeper ZOOKEEPER-SERVICE-NAME:2181 --replication-factor 1 --partitions 1 --topic mytopic
      for example:
      > kubectl --namespace kube-kafka exec -it kafka-0 -- kafka-topics.sh --create --zookeeper zookeeper.kube-kafka.svc.cluster.local:2181 --replication-factor 1 --partitions 1 --topic mytopic
```

Start a Kafka message consumer. This consumer will connect to the cluster and retrieve and display messages as they are
published to the __mytopic__ topic. Replace the _KAFKA-SERVICE-NAME_ placeholder with
the [Apache Zookeeper](https://zookeeper.apache.org) service name obtained earlier:

```
      > kubectl --namespace kube-kafka exec -it <name of kafka pod> -- kafka-console-consumer.sh --bootstrap-server KAFKA-SERVICE-NAME:9092 --topic mytopic --consumer.config /opt/bitnami/kafka/config/consumer.properties
      for example:
      > kubectl --namespace kube-kafka exec -it kafka-0 -- kafka-console-consumer.sh --bootstrap-server kafka.kube-kafka.svc.cluster.local:9092 --topic mytopic --consumer.config /opt/bitnami/kafka/config/consumer.properties
```

Using a different console, start a Kafka message producer and produce some messages by running the command below and
then entering some messages, each on a separate line. Replace the KAFKA-SERVICE-NAME placeholder with
the [Apache Zookeeper](https://zookeeper.apache.org)
service name obtained earlier:

```
      > kubectl --namespace kube-kafka exec -it <name of kafka pod> -- kafka-console-producer.sh --broker-list KAFKA-SERVICE-NAME:9092 --topic mytopic --producer.config /opt/bitnami/kafka/config/producer.properties
      for example:
      > kubectl --namespace kube-kafka exec -it <name of kafka pod> -- kafka-console-producer.sh --broker-list kafka.kube-kafka.svc.cluster.local:9092 --topic mytopic --producer.config /opt/bitnami/kafka/config/producer.properties
```

The messages should appear in the Kafka message consumer.

[Deploy a Scalable Apache Kafka/Zookeeper Cluster on Kubernetes with Bitnami and Helm](https://docs.bitnami.com/tutorials/deploy-scalable-kafka-zookeeper-cluster-kubernetes)

That's it! Microservices infrastructure [backing services](https://12factor.net/backing-services) is up and running. We can start deploying microservices.

### Installing and configuring Ingress on K8S cluster

#### 1. Creating namespace for RPS game microservices (if not exists)

First, we need to create a namespace for RPS game microservices and Ingress.
To create a _rps-app-dev_ namespace on the K8S cluster, run:

```
     > kubectl apply -f ./k8s/namespaces/rps-app-ns.yml
```

To check the status, run:

```
     > kubectl get namespaces --show-labels
```

#### 2. Deploying Simple Fanout Ingress for RPS microservices (if not exists)

To create a [Simple Fanout Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) for the RPS microservices, run:

You can select one of the following pre-configured Ingress resources:

a) Without [TLS](https://en.wikipedia.org/wiki/Transport_Layer_Security):

```
     > kubectl apply -f ./k8s/ingress/rps-ingress.yml
```

b) With [TLS](https://en.wikipedia.org/wiki/Transport_Layer_Security) (server certificate is required, see below):

```
     > kubectl apply -f ./k8s/ingress/rps-tls-ingress.yml
```

c) With [mTLS](https://en.wikipedia.org/wiki/Mutual_authentication) (server and client certificates are required, see below):

```
     > kubectl apply -f ./k8s/ingress/rps-mtls-ingress.yml
```

__Note:__ A RPS application [Simple Fanout Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress) configuration routes traffic from a single IP address to more than one.

![Simple Fanout Ingress](https://d33wubrfki0l68.cloudfront.net/36c8934ba20b97859854610063337d2072ea291a/28e8b/docs/images/ingressfanout.svg)

Make sure the RPS application ingress has been created:

```
     > kubectl get ingress -n rps-app-dev
```

__Note:__ Note for the ingress rule to take effect it needs to be created in the same namespace as the service.

You should see the following output:

```
      NAME               CLASS   HOSTS                                                                             ADDRESS        PORTS   AGE
      rps-grpc-ingress   nginx   grpc.rps.cmd.internal,grpc.rps.qry.internal,grpc.score.cmd.internal + 1 more...   192.168.49.2   80      12m
      rps-ingress        nginx   rps.internal                                                                      192.168.49.2   80      12m
```

The first [Ingress](https://kubernetes.github.io/ingress-nginx/examples/grpc) routes the gRPC API traffic. The second one routes the REST API traffic.

Note the ip address (192.168.49.2) displayed in the output, as you will need this in the next step.

Confirm that the ingress works with the following command:

```
      > kubectl describe ing rps-ingress -n rps-app-dev
```

You should see the following output:

```
    Name:             rps-ingress
    Labels:           <none>
    Namespace:        rps-app-dev
    Address:          192.168.49.2
    Ingress Class:    nginx
    Default backend:  <default>
    Rules:
      Host          Path  Backends
      ----          ----  --------
      rps.internal
                    /rps-cmd-api     rps-cmd-service-svc:8080 (10.244.0.76:8080)
                    /rps-qry-api     rps-qry-service-svc:8080 (10.244.0.54:8080)
                    /score-cmd-api   score-cmd-service-svc:8080 (10.244.0.62:8080)
                    /score-qry-api   score-qry-service-svc:8080 (10.244.0.72:8080)
    Annotations:    <none>
    Events:
      Type    Reason  Age                    From                      Message
      ----    ------  ----                   ----                      -------
      Normal  Sync    2m19s (x2 over 2m40s)  nginx-ingress-controller  Scheduled for sync
```

Repeat the same step for another ingress of _rps-grpc-ingress_.

__Note:__ All tls ingresses terminate tls at Ingress level. You should see the following lines in the above log:

```
      TLS:
        rps-tls-secret terminates rps.internal
      and
      TLS:
        rps-cmd-service-grpc-tls-secret terminates grpc.rps.cmd.internal
        rps-qry-service-grpc-tls-secret terminates grpc.rps.qry.internal
        score-cmd-service-grpc-tls-secret terminates grpc.score.cmd.internal
        score-qry-service-grpc-tls-secret terminates grpc.score.qry.internal
```

[TLS Termination](https://kubernetes.github.io/ingress-nginx/examples/tls-termination)

#### 3. Adding custom entry to the etc/host file for the RPS game microservices (if not exists)

Add a custom entry to the etc/hosts file using the nano text editor:

```
     > sudo nano /etc/hosts
```

You should add the following ip address (copied in the previous step) and custom domains to the hosts file:

```
      192.168.49.2 rps.internal grpc.rps.cmd.internal grpc.rps.qry.internal grpc.score.cmd.internal grpc.score.qry.internal
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

Repeat the same step for other custom domain names of _grpc.rps.cmd.internal_, _grpc.rps.qry.internal_, _grpc.score.cmd.internal_, _grpc.score.qry.internal_.

### Generating self-signed server certificate (TLS connection) with [OpenSSL](https://www.openssl.org)

#### 1. Generation Public self-signed certificate

Generate a public CA key and certificate with the following command:

```
      > openssl req -x509 -sha256 -newkey rsa:4096 -days 3560 -nodes -keyout rps-public-ca.key -out rps-public-ca.crt -subj '/CN=RPS Public Cert Authority/O=RPS Public CA'
```
__Note:__ Skip the next steps if you are not going to use [TLS](https://en.wikipedia.org/wiki/Transport_Layer_Security) connection for dev environment.

#### 2. Generation self-signed server certificate

Generate a self-signed server certificate for the _rps.internal_ host with the following command:

```
     > openssl req -new -nodes -newkey rsa:4096 -out rps.internal.csr -keyout rps.internal.key -subj '/CN=rps.internal/O=rps.internal'
```

Sign in the generated SSL server certificate with public CA certificate by executing the following command:

```
     > openssl x509 -req -sha256 -days 365 -in rps.internal.csr -CA rps-public-ca.crt -CAkey rps-public-ca.key -set_serial 01 -out rps.internal.crt
```

You should see the following output:

```
    Certificate request self-signature ok
    subject=CN = rps.internal, O = rps.internal
```

At this point, we have a signed server certificate _rps.internal.crt_ and key _rps.internal.key_ which needs to be defined to the Kubernetes cluster through a Kubernetes secret resource.
The following command will create a secret named _rps-tls-secret_ that holds the server certificate and the private key:

```
     > kubectl create secret tls rps-tls-secret --key rps.internal.key --cert rps.internal.crt -n rps-app-dev
```

You will see the following output:

```
      secret/rps-tls-secret created
```

It means that the secret has successfully been created. This secret is used to validate the server's identity.
To view secrets execute the following command:

```
      > kubectl get secrets -n rps-app-dev
```

You should see the following output:

```
      NAME                       TYPE                DATA   AGE
      rps-tls-secret             kubernetes.io/tls   2      19s
```

__Note:__ The _rps-tls-secret_ secret is of type kubernetes.io/tls.

If you deploy _rps-tls-ingress_ Ingress instead of the _rps-ingress_ one and execute the following command:

```
      > curl -k -v https://rps.internal
```

__Note:__ -k flag is used to skip self-signed certificate verification, -v flag is used to get verbose fetching.

You should see the following output:

```
      *   Trying 192.168.49.2:443...
      * Connected to rps.internal (192.168.49.2) port 443 (#0)
      * ALPN, offering h2
      * ALPN, offering http/1.1
      * TLSv1.0 (OUT), TLS header, Certificate Status (22):
      * TLSv1.3 (OUT), TLS handshake, Client hello (1):
      * TLSv1.2 (IN), TLS header, Certificate Status (22):
      * TLSv1.3 (IN), TLS handshake, Server hello (2):
      * TLSv1.2 (IN), TLS header, Finished (20):
      * TLSv1.2 (IN), TLS header, Supplemental data (23):
      * TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
      * TLSv1.2 (IN), TLS header, Supplemental data (23):
      * TLSv1.3 (IN), TLS handshake, Certificate (11):
      * TLSv1.2 (IN), TLS header, Supplemental data (23):
      * TLSv1.3 (IN), TLS handshake, CERT verify (15):
      * TLSv1.2 (IN), TLS header, Supplemental data (23):
      * TLSv1.3 (IN), TLS handshake, Finished (20):
      * TLSv1.2 (OUT), TLS header, Finished (20):
      * TLSv1.3 (OUT), TLS change cipher, Change cipher spec (1):
      * TLSv1.2 (OUT), TLS header, Supplemental data (23):
      * TLSv1.3 (OUT), TLS handshake, Finished (20):
      * SSL connection using TLSv1.3 / TLS_AES_256_GCM_SHA384
      * ALPN, server accepted to use h2
      * Server certificate:
      *  subject: CN=rps.internal
      *  start date: Jun  4 20:13:01 2023 GMT
      *  expire date: Jun  1 20:13:01 2033 GMT
      *  issuer: CN=rps.internal
      *  SSL certificate verify result: self-signed certificate (18), continuing anyway.
```

You can see that self-signed server certificate has successfully been verified.

Repeat the same steps for 4 grpc server certificates. Make sure to change custom domain to a corresponding gprc one:

```
      -subj '/CN=rps.internal/O=rps.internal'
      to
      -subj '/CN=grpc.rps.cmd.internal/O=grpc.rps.cmd.internal'
      -subj '/CN=grpc.rps.qry.internal/O=grpc.rps.qry.internal'
      -subj '/CN=grpc.score.cmd.internal/O=grpc.score.cmd.internal'
      -subj '/CN=grpc.score.qry.internal/O=grpc.score.qry.internal'
```

__Note:__ Skip the next steps if you are not going to use [mTLS](https://en.wikipedia.org/wiki/Mutual_authentication) connection for dev environment.

#### 3. Generating client certificate (mTLS connection)

Generate CA "Certificate Authority" certificate and key with the following command:

```
      > openssl req -x509 -sha256 -newkey rsa:4096 -keyout ca.key -out ca.crt -days 356 -nodes -subj '/CN=RPS Cert Authority'
```

Then apply the CA as secret to kubernetes cluster with the following command:

```
      > kubectl create secret generic ca-secret --from-file=ca.crt=ca.crt -n rps-app-dev
```

You should see the following output:

```
      secret/ca-secret created
```

It means that the secret has successfully been created. This secret is used to validate client certificates.
Validating the ([mTLS](https://en.wikipedia.org/wiki/Mutual_authentication)) connection.

Next we generate a client [Certificate Signing Request (CSR)](https://en.wikipedia.org/wiki/Certificate_signing_request) and client key with the following command:

```
      > openssl req -new -newkey rsa:4096 -keyout rps.client.key -out rps.client.csr -nodes -subj '/CN=RPS Client'
```

Then we sign in the [Certificate Signing Request (CSR)](https://en.wikipedia.org/wiki/Certificate_signing_request) with the CA certificate by executing the following command:

```
      > openssl x509 -req -sha256 -days 365 -in rps.client.csr -CA ca.crt -CAkey ca.key -set_serial 02 -out rps.client.crt
```

You should see the following output:

```
      Certificate request self-signature ok
      subject=CN = RPS Client
```

It means that the client certificate and key have successfully been generated.

Verifying [mTLS](https://en.wikipedia.org/wiki/Mutual_authentication) connection: 

First, try to curl without client certificate:

```
      > curl -vk https://rps.internal
```

You should see the following output:

```
      <html>
      <head><title>400 No required SSL certificate was sent</title></head>
      <body>
      <center><h1>400 Bad Request</h1></center>
      <center>No required SSL certificate was sent</center>
      <hr><center>nginx</center>
      </body>
      </html>
```

Then try the same call with client key and cert:

```
      > curl -vk https://rps.internal --key rps.client.key --cert rps.client.crt
```

It should do the trick this time. Make sure you can see the following lines in the log:

```
      * TLSv1.3 (IN), TLS handshake, CERT verify (15):
      ...
      * TLSv1.3 (OUT), TLS handshake, CERT verify (15):
```

As you can see the certificate verification has been made twice, one for server certificate and another one for the client certificate.

[TLS](https://kubernetes.github.io/ingress-nginx/user-guide/tls)
[Using multiple SSL certificates in HTTPS load balancing with Ingress](https://cloud.google.com/kubernetes-engine/docs/how-to/ingress-multi-ssl)

### Useful links

For testing gRPC API (make sure that you are using correct grpc port for a profile), please consider the following
options:

* [BloomRPC GUI client for gRPC](https://github.com/bloomrpc/bloomrpc)
* [gRPCurl command-line tool](https://github.com/fullstorydev/grpcurl)
* [gRPC UI command-line tool](https://github.com/fullstorydev/grpcui)
* [Test gRPC services with Postman or gRPCurl in ASP.NET Core](https://learn.microsoft.com/en-us/aspnet/core/grpc/test-tools?view=aspnetcore-3.1)

For testing REST API, you can also consider the following options:

* [Postman GUI client for REST](https://www.postman.com)
* [Postman Now Supports gRPC](https://blog.postman.com/postman-now-supports-grpc)

For testing MongoDB, you can also consider the following options:

* [Compass GUI for MongoDB](https://www.mongodb.com/products/compass)
* [Robo 3T for MongoDB](https://robomongo.org)

To get an idea of HTTP/2 performance, you can follow the link below:

* [HTTP/2 Demo](http://www.http2demo.io)

Kubernetes

* [K8S Cluster-level Logging Architecture](https://kubernetes.io/docs/concepts/cluster-administration/logging/#using-a-node-logging-agent)
* [K8S Dashboard](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard)
* [K8S Ingress Installation Guide](https://kubernetes.github.io/ingress-nginx/deploy/#quick-start)
* [K8S Overview for MariaDB Users](https://mariadb.com/kb/en/kubernetes-overview-for-mariadb-users)
* [K8S Monitoring Stack Configuration](https://github.com/prometheus-operator/kube-prometheus)

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
