# Service Register & Service Discovery

- [Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/#netflix-eureka-client-starter)


## Missing answers:

- We need to decouple the microservices from each other.

```java
TransitPath responseValue = 
        restTemplate.getForObject(
                "http://127.0.0.1:8082/routing", 
                TransitPath.class);
```

- this is problematic:
  - The application knows deployments details about the routing service, host and port.
  - If the routing changes then we will have to update all the service that is call him.


## Service Register

- We want a logical mapping from a service’s `ID` to the `hosts` and `ports` (the nodes) on which that service is available. 
- A service registry is a good fit.


Spring Cloud provides the Discovery Client abstraction to make it easy for clients to work with different types of service registries.

- Apache Zookeeper
- Hashicorp
- Consul
- Netflix Eureka

In the Spring Cloud would the Netflix Eureka the most used one. It is served by Netflix, scales very well, and is also easy enough to install and run.

While Netflix Eureka can be configured to do virtually anything, you shouldn’t undertake the work lightly. There’s a lot of value in having proven recipes for **security**, **scale**, and **redundancy**, **such as what you get when using spring cloud service-based Netflix installation on Pivotal Cloud Foundry or Pivotal Web Services**.

Hopefully, if you are using a platform (like Cloud Foundry) that can automate the operational aspect of running something like this for you.


## Configuring Eureka server

1. using [spring initializr](https://start.spring.io/), add the dependency of `Eureka Server`
```xml
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

  - This is just the very basic initial necessary configuration to get started.
  - For production environments you need to pay attention in certain aspects like, **security**, **scale**, and **redundancy**. 



2. Then you will need to initialize it using `@EnableEurekaServer`, as show below:
```java
@EnableEurekaServer
@SpringBootApplication
public class ServiceRegistryApplication {
    
}
```

3. You will need to configure the service registry. It’s a common convention to start the service on port `8761`, and we don’t want the registry to try to register itself with other nodes.
```yaml
server:
  port: 8761
eureka:
  client:
    # this is a eureka server, we don't have to fetch registry from any other place.
    fetch-registry: false
    register-with-eureka: false
  # eureka.server.enable-self-preservation=false If a significant portion of the registered instances stops delivering heartbeats within a time span,
  # Eureka assumes the issue is due to the network and does not de-register the services that are not responding.
  # This is Eureka self-preservation mode.
  # Leaving this set to true is probably a good idea,
  # but it also means that if you have a small set of instances th a few nodes then you won't see the expected
  # behavior when instances de-register.
  # server:
  #  enable-self-preservation: false

  freemarker:
    template-loader-path: classpath:/templates/
    prefer-file-system-access: false
```

4. We are ready to go, we can start up the Service Discovery service

```shell

java -jar cloud/service-registry/target/service-registry-0.0.1-SNAPSHOT.jar

$ http://localhost:8761/lastn

$ curl -L -m 500 -X GET http://localhost:8761/eureka/apps
```

## Configuring downstream services

```xml
<!-- Service register-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- Actuator -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
## Service Discovery
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
    healthcheck:
      enabled: true
```
