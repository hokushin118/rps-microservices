# RPS Game gRPC Library Project

The RPS Game gRPC library project contains Rock Paper Scissors game messages and service descriptions in the proto 3 format.

### Prerequisites

* Java 11 or higher

### Technology stack

* [OpenJDK 11](https://openjdk.java.net/projects/jdk/11)  
* [Maven 3.6.0](https://maven.apache.org)  
* [gRPC framework 1.32.1](https://grpc.io/docs/languages/java/quickstart)

### Building the RPS Game gRPC library project from the command line

* Navigate to the common/rps-grpc-lib directory on your computer.

```
    > cd common/rps-grpc-lib
```

* And run _mvn clean install_ in the root directory of the rps-grpc-lib project to generate Java model classes and service descriptions for microservices from proto3 models.

```
     > mvn clean install
```

### gRPC

gRPC is a modern, lightweight communication protocol from Google. gRPC is a high-performance, open source, universal RPC framework that can run in any environment.

gRPC advantages:

* It is polyglot.  
* Bi-directional support: gRPC takes advantage of HTTP/2’s bi-directional communication support, removing the need to separately support request/response alongside websockets, SSE, or other push-based approaches on top of HTTP/1.
* It is strongly typed.  
* It is platform agnostic.  
* Reduced network latency: gRPC builds on HTTP/2, which allows for faster and long-lived connections, reducing the time for setup/teardown common for individual HTTP/1.x requests.
* Flexible schemas with backward compatibility that can evolve over time.
* Support for comments and documentation.
* Protocol buffers are binary or machine readable and can be used to exchange messages between services and not over browsers. Payload size is also very tiny.
* Infrastructure support: Those selecting gRPC are often using Kubernetes on Google Kubernetes Engine (GKE), which provides built-in proxy and load balancing support.
  
gRPC disadvantages:

* Longer learning curve.
* Quite limited when it comes to browser support.
* Longer bootstrap time – initial setup takes time compared to REST.
* Lacks human readability – leading to complexity in troubleshooting problems.
* Limited community support.

[gRPC - A Modern Framework for Microservices Communication](https://www.capitalone.com/tech/software-engineering/grpc-framework-for-microservices-communication)