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
curl http://127.0.0.1:8888/booking-ms/default | jq .
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


**2. create the bootstrap.yml file in your applications:**
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
