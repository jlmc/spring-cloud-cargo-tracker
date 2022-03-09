# Config Server 

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
```
curl http://127.0.0.1:8888/booking-ms/default | jq .
```
