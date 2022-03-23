# Configuration server

```shell
docker-compose up rabbitmq postgres zipkin -d
```

## 1. Create Configuration Server

Using [spring initializr](https://start.spring.io/) we should create a project the one required dependency:
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

and to connect to cloud server event bus
```xml
<!-- Event bus -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-monitor</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
<!-- / Event bus -->
```

### 1.1. GitHub configuration

1. show the file: [cloud/config-server/src/main/resources/application-prod.yaml](/cloud/config-server/src/main/resources/application-prod.yaml)

2. start the application server in prod profile.
```shell
java -jar cloud/config-server/target/config-server-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

3. Show some requests, and talk about it.

- HTTP endpoint: `curl http://127.0.0.1:8888/{application-name}/{profile}/{label}`

  - application-name: `spring.application.name`
  - profile: `spring.profiles.active`
  - label: git branch name


```shell

$ curl http://127.0.0.1:8888/bookingms/default | jq .

$ curl http://127.0.0.1:8888/bookingms/qa | jq .

$ curl http://127.0.0.1:8888/bookingms/default/prod | jq .

$ curl http://127.0.0.1:8888/warehouse-service/default | jq .
```

In the response, pay attention on the fields:

- `version`: it is the hash of the configuration version
- `propertySources.name`: localisation file
- `propertySources.source`: configuration content



### 1.2. Local Folder configuration

```shell
java -jar cloud/config-server/target/config-server-0.0.1-SNAPSHOT.jar
```

- Note that:

- If we change any of the configurations files:
  - The configuration server will know, it will detect the that change.
  - An Event will be propagated to the event bus. `RefreshScopeRefreshedEvent`



---

## 2. Connect with configuration server 

1. to connect with connection server it is necessary to add the dependencies:
```xml
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
```

### 2.1. Bootstrap 

Spring cloud provide a project that allow to execute load part of the application before load all the Spring Application context:  

`org.springframework.cloud:spring-cloud-starter-bootstrap`

- This `bootstrap` file gets load earlier than other properties files (including the application.properties).

- in this file we can configure some cloud infrastructure configurations. 

- [bookingms/src/main/resources/bootstrap.yaml](/bookingms/src/main/resources/bootstrap.yaml)

### 2.2. @RefreshScope Spring Cloud special Scope

1. start Service Discovery
2. start bookingms

```shell
curl localhost:8081/ping -d {} -H "Content-Type: application/json" | jq .
```

- Actuator
```shell
$ curl localhost:8081/actuator/refresh -d {} -H "Content-Type: application/json"
```

- Event Bus
