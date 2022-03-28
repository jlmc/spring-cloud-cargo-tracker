## PRESENTATION

```shell
docker-compose up rabbitmq postgres zipkin -d
```

## 1. create the api-gateway project

- [https://start.spring.io/](https://start.spring.io/)
  - Gateway SPRING CLOUD ROUTING
  - Eureka Discovery Client SPRING CLOUD DISCOVERY

1. You need to create a new project for the API-GATEWAY having the following mandatory dependencies:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. In the API-GATEWAY project, some configurations have to be made, like the server port and application name:

```yaml
server:
  port: 9090

spring:
  application:
    name: api-gateway
```


3. Enable the Service Discovery client (Eureka):
```java
@EnableEurekaClient

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

>  If your services are registered in a service registry for which there is a Spring Cloud `DiscoveryClient` implementation configured, then it’s a simple matter of adding an `@EnableZuulProxy` to your application code (along with org.springframework.cloud : spring-cloud-starter-zuul to the classpath); then Spring Cloud, working with the `DiscoveryClient`, will set up routes that proxy downstream services for you

4. Provider the Eureka server url and other necessary configurations in the application.yaml
```yaml
## Service Discovery - Eureka
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
  instance:
    # what distinct ID do we want for each registered service, we can override the default attribution
    # instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
    # instance-id: ${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}}
    instance-id: ${spring.application.name}:${spring.application.instance_id:${server.port}}:${random.value}
    lease-renewal-interval-in-seconds: 30
```

5. So now we know that our Api-Gateway application will work with Eureka server, to allow/route the requests to the appropriated downstream microservices. But for that we need to configure it. It is necessary to enable the discovery locator for our api gateway.

```properties
spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```



## Testing out

```shell

docker-compose up rabbitmq postgres zipkin -d

## start up the config server
java -jar cloud/config-server/target/config-server-0.0.1-SNAPSHOT.jar

## start Eureka server
java -jar cloud/service-registry/target/service-registry-0.0.1-SNAPSHOT.jar

## start the downstream microservices
java -jar routingms/target/routingms-0.0.1-SNAPSHOT.jar --server.port=7777
java -jar routingms/target/routingms-0.0.1-SNAPSHOT.jar --server.port=8082
java -jar routingms/target/routingms-0.0.1-SNAPSHOT.jar --server.port=8184


java -jar bookingms/target/bookingms-0.0.1-SNAPSHOT.jar --server.port=7081
java -jar bookingms/target/bookingms-0.0.1-SNAPSHOT.jar --server.port=8081


## start api gateway
java -jar cloud/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar --server.port=9090

## See the apps in Service Register
curl -L -m 500 -X GET http://localhost:8761/eureka/apps
```

Now instead o call directly the downstream microservices we can call the api-gateway, for example:


```shell
curl -L -m 500 -X POST 'localhost:9090/bookingms/booking' \
-H 'Content-Type: application/json' \
--data-raw '{
"bookingAmount": 12,
"originLocation": "ABC",
"destLocation": "DEF",
"destArrivalDeadline": "2022-04-01T21:23:43.598697Z"
}' | jq .


curl -L -m 500 -H'Authorization: GOAT' -H'X-SYS: x1' -X POST 'localhost:9090/bookingms/booking/ef622f7a/route'
```


## API-Gateway load balance

Now we want that all inter-service-communication (between the downstream microservices), should be made using the api gateway as proxy/bridge

  - you must change the configuration to use the api-gateway.:
  
1. having this class as example existing implementation:
```java
@FeignClient(
        name = "api-gateway", // application-name of the api-gateway, value = "routingms",
        contextId="routingmsOpenfeign",
        // configuration = FeignClientConfig.class,
        decode404 = true)
public interface CargoRoutingOpenfeign {

  //private static final String ROUTING_MS = "http://routingms/routing";

  @GetMapping(path = "/routingms/routing", produces = MediaType.APPLICATION_JSON_VALUE)
  TransitPath findOptimalRoute(
          @RequestParam(value = "origin", required = false) String originUnLocode,
          @RequestParam(value = "destination", required = false) String destinationUnLocode,
          @RequestParam(value = "deadline", required = false) String deadline
  );
}

```


##  Apply Api Filters

### Pre-Filters

```shell
curl -L -i -m 500 -H'Authorization: GOAT' -H'X-SYS: x1'  -X POST 'localhost:9090/bookingms/booking/ef622f7a/route'
```

## Cross-Origin Resource Sharing (CORS)

-About [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)

- [Spring api gateway CORS configuration](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#cors-configuration)

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            # allowedOrigins: "https://www.javarubberduck.com"
            allowedOrigins: "*"
            allowedHeaders:
              - content-type
            allowedMethods:
              - GET
              - POST
              - DELETE
              - PUT
              - PATCH
              - HEAD
```

## HTTP Forwarding headers

The api gateway forward the request headers to the downstream services:

```
curl -L -i -m 500 -H'Authorization: GOAT' -H'X-SYS: x1'  -X POST 'localhost:9090/bookingms/booking/ef622f7a/route'
```

Check the loggers in `bookingms`.
