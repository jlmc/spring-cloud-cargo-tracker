# 2 Service Discovery


We want a logical mapping from a service’s **ID** to the **hosts** and **ports** (the nodes) on which that service is available. A service registry is a good fit.

Spring Cloud provides the DiscoverClient abstraction to make it easy for clients to work with different types of service registries.

- Apache Zookeeper
- Hashicorp
- Consul
- Netflix Eureka

**In the Spring Cloud world the Netflix Eureka the most used one. it is served by Netflix, scales very well, and is also easy enough to install and run.**

While Netflix Eureka can be configured to do virtually anything, you shouldn’t undertake the work lightly. There’s a lot of value in having proven recipes for security, scale, and redundancy, such as what you get when using spring cloud service-based Netflix installation on Pivotal Cloud Foundry or Pivotal Web Services.


## Configuring Eureka server

1. In Order to set up a Eureka service registry, you will need the dependency, `org.springframework.cloud:spring-cloud-starter-netflix-eureka-server`, in a spring boot project.

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

2. Then you will need to initialize it using @EnableEurekaServer, as show below package io.github.jlmc.cloudserviceregistryserver;
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
public class CloudServiceRegistryServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CloudServiceRegistryServerApplication.class, args);
  }

}
```

_This is not a production-worthy configuration! There are a lot of things we are conveniently hand-waving away in order to get things working and to demonstration concepts. You will need to care for these things in the production environment. Netflix Eureka can handle your use cases, but that doesn’t always mean that things like clustering are easy._

  - https://github.com/spring-cloud/spring-cloud-netflix/issues/203
  
_Hopefully, you are using a platform (like Cloud Foundry) that can automate the operational aspect of running something like this for you._


3. You will need to configure the service registry. It’s a common convention to start the service on port `8761`, and we don’t want the registry to try to register itself with other nodes.
```yaml
# server.port=8761
server:
  port: 8761

eureka:
  client:
    # this is a eureka server, we don't have to fetch registry from any other place.
    fetch-registry: false
    register-with-eureka: false
  server:
    enable-self-preservation: false
```

Some considerations:

- `eureka.client.register-with-eureka=false` and `eureka.client.fetch-registry=false`
  - We don’t want Eureka to try to register with itself.

- `eureka.server.enable-self-preservation=false`
  - If a significant portion of the registered instances stops delivering heartbeats within a time span, Eureka assumes the issue is due to the network and does not de-register the services that are not responding. This is Eureka’s self-preservation mode. Leaving this set to true is probably a good idea, but it also means that if you have a small set of instances (as you will if you are trying these examples with a few nodes on a local machine) then you won't see the expected behavior when instances de-register.

4. We are ready to go, you can startup the applications, we can see some useful information in the Eureka server website: 
  - `http://localhost:8761/lastn`

5. We can also see the applications already registered on the Service discovery:

```shell
curl -L -m 500 -X GET http://localhost:8761/eureka/apps
```
