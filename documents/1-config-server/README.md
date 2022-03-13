# Config Server 


Spring boot  have Bootiful Configuration strategy.
it improves configuration things considerably. Spring boot will automatically load properties from a hierarchy of well-known places by default. The command-line arguments override properties values contributed from  JNDI, which override properties contributed from System.getProperties(), etc:

But for example, We may have a common set of properties for each of your microservices. For example, database properties (username, password).


#### Centralized, Journaled Configuration with the Spring Cloud-Configuration-Server.

We will  need to do better. We are still missing answers for some common use cases:

1. Changes to an application’s configuration, as configured, would require a restart.

2. There is no traceability: how do we determine what changes were introduced into production and, if necessary, roll back those changes?

3. Configuration is decentralized; it’s not immediately apparent where to go to change what.

4. There is no out-of-the-box support for encryption and decryption for security.


#### The Spring Cloud Config Server

We could address the configurations to a centralized by storing configuration in a single directory and pointing all applications to that directory. 
 
We could also version could the directory, using  **GIT**  or **SUbversion**. That would give us the support we desire for auditing and journalling. It still doesn’t solve the last two requirements; we need something a bit more sophisticated.

> https://cloud.spring.io/spring-cloud-config/reference/html/

The **Spring Cloud Config Server** is a REST API to which our clients will connect to draw their configuration. It manages a version-controlled repository of configuration, too. It sits in between our clients and the repository of configuration and so is in an enviable position to interpose security on the communication from clients to the service and on the communication from the service to the version-controlled repository of configuration.
The Spring Cloud Config client contributes a new scope to client applications, the **refresh scope** `@RefreshScope`, that allows us to reconfigure Spring components without restarting the application.

⚠️ _Technologies like spring cloud config server are important, but they will add cost to your operational overhead. Ideally, this competency should be managed by the platform, and automated. If you are using Cloud Foundry, there is a Config Server service, in the service catalog._



---

## 1. Create the configuration server

using [spring initializr](https://start.spring.io/) we should create a project the one required dependency:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

```
org.springframework.cloud:org.springframework.cloud
```
## 2. testing with native mode

- We can create a local folder:
```
/Users/joao.morgado/Documents/junks/projects/spring/cloud/spring-cloud-cargo-tracker/cloud/config-repo
```

- Create a file name with same name of the applied pattern spring.application.name .properties and .yaml (booking-ms.properties)
  - The name pattern should be `[Application-Name]-[Profile].yaml`

- starting the config server, we are able to get the configuration of the configured application.
```shell
curl http://127.0.0.1:8888/bookingms/default | jq .
```


## 3. Configure the downstream service to config server

**1. add the necessary dependencies:**

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-client</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
  </dependency>

  <!-- others dependencies -->
</dependencies>
```
  - The `org.springframework.cloud:spring-cloud-config-client` will be used to configure the connection with the configuration server.
  - The `org.springframework.cloud:spring-cloud-starter-bootstrap` will be used to allow the use of the `bootstrap.properties` file.
    - This bootstrap file gets load earlier than other properties files (including the `application.properties`).
      The configuration server manages a directory full of *.properties files. Spring Cloud server configuration to connected clients by matching the client spring.application.name. Thus, a configuration client that identifies itself as foo-service would see the configuration in foo-service.properties.


**2. create the `bootstrap.yml` file in your applications:**
```yaml
server:
  port: 8081
spring:
  application:
    name: booking-ms
  profiles:
    active: default, dev

  cloud:
    config:
      uri: http://localhost:8888
      profile: default
```
- 👆 we need to configure the application name.
- 👆 the address of the configuration server and the profile name that will be fetched.

**3. there is no need to use the application.yaml file, because all the properties will be fetched from the configuration file.**

---

## Refreshable Configuration 

Centralized configuration is a powerful thing but changing the configuration isn’t immediately visible to the beans that depend on it. Spring Cloud refresh scope, with **`@RefreshScope`** annotation, solve this problem: 

- `@org.springframework.cloud.context.config.annotation.RefreshScope`

- 💡 The `@RefreshScope` makes the bean refreshable. This annotation is a Spring Cloud-contributed scope that lets  any bean recreate itself in place, whenever a refresh event is triggered.
- 💡Fundamentally, all the refresh-scoped beans will refresh themselves when they receive a Spring `ApplicationContext` event of the type `RefreshScopeRefreshEvent`.
- 💡 There are various ways to trigger the refresh event. You can trigger the refresh by sending an empty:
  `POST http://127.0.0.1:8080/actuator/refresh`, which is a Spring Boot Actuator endpoint that is exposed automatically.
  `curl -H'Content-Type:application/json' -d{} http://localhost:8081/actuator/refresh`

1. In the downstream service we need to add and configure the actuator dependency:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. Enable the refresh actuator endpoint, in this example we are enabling all the endpoints, you can see all the endpoints in: http://localhost:8081/actuator.

  - This configuration should be done in the application.properties file, not in the bootstrap.properties. so, we can configure this endpoint in the file of the configuration repository.
```yaml
## Actuator
### $ curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"
management:
  security:
    enabled: false
  endpoints:
    web:
      exposure:
        include: '*'
```

Now, if we execute the following request, all the beans annotated with `@RefreshScope` will be recreated using the updated configuration.
```shell
curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"
```

- By using Actuator, we can refresh clients. However, in the cloud environment, we would need to go to every single client and reload configuration by accessing actuator endpoint. To solve this problem, we can use Spring Cloud Bus.

---

### Configure with refresh using Spring Cloud Bus

#### In configuration server

```xml
<ds>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-monitor</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
</ds>
```

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

The Spring Cloud Bus exposes a different Actuator endpoint, `/bus/refresh`, that will publish a message to the connected RabbitMQ broker that will trigger all connected nodes to refresh themselves. You can send the following message to any node with the spring-cloud-starter-bus-amqp autoconfiguration, and it'll trigger a refresh in all the connected nodes.

```
curl localhost:8081/bus/refresh -d {} -H "Content-Type: application/json"
```

#### in downstream services

```xml
<dep>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-actuator-autoconfigure</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
    </dependency>
</dep>
```

3. in the bootstrap.yaml file present in the downtime service.
```yaml
server:
  port: 8081

spring:
  application:
    name: bookingms
  profiles:
    active: default, dev

  cloud:
    config:
      uri: http://localhost:8888
      profile: default
```

3. In the application.yaml, present in the config repository:

```yaml
booking:
  message: Hello message from config server Ya!!!

## Actuator
### $ curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"
management:
  security:
    enabled: false
  endpoints:
    web:
      exposure:
        include: '*'

spring:
  cloud:
    bus:
      enabled: true
      refresh:
        enabled: true

  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    
# and all the other properties    
```

- 🚀  Having done this, the update 


## References:

- [https://spring.io/guides/gs/centralized-configuration/](https://spring.io/guides/gs/centralized-configuration/)
- [https://spring.io/guides/gs/multi-module/](https://spring.io/guides/gs/multi-module/)
- [https://www.baeldung.com/spring-cloud-bus](https://www.baeldung.com/spring-cloud-bus)
- [https://github.com/eugenp/tutorials/tree/master/spring-cloud-bus](https://github.com/eugenp/tutorials/tree/master/spring-cloud-bus)
