# 8 API-Gateway-with-Spring

🚀 [Cloud Spring Api Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-starter)

🚀 [CORDS](https://javarubberduck.com/java/cors-gateway/)

Since some time now, the spring team has stopped supporting **Netflix Zuul**, this project has been replaced in spring-cloud framework by the new spring-api-gateway implementation.
Earlier Spring was providing Zuul API Gateway, but now it is being deprecated and Spring Cloud API Gateway is being used.

Basically the both project provide the same features.

- Filters can be used to create a top-level context for requests into a system.
- Handling concerns like geolocation.
- Cookie propagation, and token decryption.
- Address cross-cutting concerns like authentication


- Communicate with Eureka.
- Having all information about the nodes, it can redirect the HTTP requests to the right services.
- Load balance, because now the load balance can be made in the api-gateway instead of in the downstream microservices.
- Allow implement some important aspect like authentications and authorization for each HTTP request.
- Allow implement some features like rate-lime.


Earlier Spring was providing Zuul API Gateway but now its been deprecated and Spring Cloud API Gateway is being used.

Always go with the latest option available in market.

## 1. create the api-gateway project

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

docker-compose up rabbitmq postgres -d

## start up the config server
java -jar cloud-config-server/target/cloud-config-server-0.0.1-SNAPSHOT.jar

## start Eureka server
java -jar cloud-service-registry-server/target/cloud-service-registry-server-0.0.1-SNAPSHOT.jar

## start the downstream microservices
java -jar warehouse/target/warehouse-0.0.1-SNAPSHOT.jar
java -jar delivery/target/delivery-0.0.1-SNAPSHOT.jar
java -jar shop/target/shop-0.0.1-SNAPSHOT.jar


## start api gateway
java -jar cloud-api-gateway/target/cloud-api-gateway-0.0.1-SNAPSHOT.jar
```

Now instead o call directly the downstream microservices we can call the api-gateway, for example:

```shell
curl -L -m 500 -X POST 'localhost:9090/shop-service/v1/orders' \
-H 'Content-Type: application/json' \
--data-raw '{
  "orderItem": {
    "productId": 1,
    "qty": 1
  },
  "address": {
    "name": "Rayan Copper",
    "street": "Bourbon Street",
    "city": "French Quarter of New Orleans",
    "zipCode": "1234-654",
    "country": "USA"
  }
}' | jq .


curl -L -m 500 -X GET 'localhost:9090/shop-service/v1/orders/1' | jq .

curl -v -X OPTIONS 'localhost:9090/shop-service/v1/orders/1'
```

## API-Gateway load balance

Now we want that all inter-service-communication (between the downstream microservices), should be made using the api gateway as proxy/bridge

1. having this class as example existing implementation:
```java
@FeignClient(name = "delivery-service", configuration = FeignClientConfig.class)
public interface DeliveryClient {

    @GetMapping(path = "/v1/deliveries/tracking/{trackingId}")
    DeliveryDTO getDeliveryDetails(@PathVariable("trackingId") String trackingId);

    @PostMapping(path = "/v1/deliveries/")
    DeliveryDTO createDeliveryTrack(@RequestBody Address address);
}
```

2. you must change the configuration to use the api-gateway, the previews' implementation must change to the following:

```java
@FeignClient(
        name = "api-gateway", // application-name of the api-gateway
        contextId="DeliveryServiceClient", // Spring bean context id to be used in this current application context
        configuration = FeignClientConfig.class)
public interface DeliveryClient {

    @GetMapping(path = "/delivery-service/v1/deliveries/tracking/{trackingId}")
    DeliveryDTO getDeliveryDetails(@PathVariable("trackingId") String trackingId);

    @PostMapping(path = "/delivery-service/v1/deliveries/")
    DeliveryDTO createDeliveryTrack(@RequestBody Address address);
}
```


##  Apply Api Filters

### Pre-Filters

```
curl -L -m 500 -X GET -H'Authorization: xxx'  'localhost:9090/shop-service/v1/orders/1' | jq .
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


---
## Anexos

### pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>io.github.jlmc</groupId>
    <artifactId>cloud-api-gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>cloud-api-gateway</name>
    <description>cloud-api-gateway</description>
    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2021.0.1</spring-cloud.version>
    </properties>
    <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-sleuth-zipkin</artifactId>
        </dependency>


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

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```
