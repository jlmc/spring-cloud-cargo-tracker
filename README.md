# Spring Cloud

- It is providing projects to quickly develop microservices.
- Spring cloud is having projects that will help the developers to quickly develop and maintain the micro-services.

- If you want to check out all the projects provided by the spring cloud, then here is the official website (https://spring.io/projects/spring-cloud).

- Cloud Foundry:  https://www.cloudfoundry.org/get-started/


## Content

* Configuration Server
* Service Discovery
* Client Load Balancer spring-feign
* Declarative REST Client
* Api-Gateway
* Distribute tracing

## Use case - Cargo tracker

![cargo-tracker](documents/ms-architecture.png)

> Demonstrate well established architectural patterns/blueprints for enterprise development with Jakarta EE using pretty close to a real world application.
>
> Demonstrate a concrete implementation of DDD concepts.

- We are going to use the well-known cargo tracker case study project.
- Of course, we will not use all the features, that wouldn't be of much interest in our examples, because we're trying to play with a different theme.

- 🚀 I selected only three use cases that will serve as a basis for all the examples:

## Use Cases

To run the examples we must run the following command to start the dependent infrastructure services:

```shell
docker-compose up rabbitmq postgres zipkin -d
```

### Use Case 1: Booking a new cargo

```shell
curl -L -m 500 -X POST 'localhost:9090/bookingms/booking' \
-H 'Content-Type: application/json' \
--data-raw '{
  "bookingAmount": 12,
  "originLocation": "ABC",
  "destLocation": "DEF",
  "destArrivalDeadline": "2022-04-01T21:23:43.598697Z"
}' | jq .
```

#### Use Case 2: Route an existing cargo

```shell
curl -L -m 500 -H'Authorization: GOAT' -H'X-SYS: x1' -X POST 'localhost:9090/bookingms/booking/623f0cbf/route'
```

#### Use Case 3: Get Cargo details

```shell
curl -L -m 500 -X GET 'localhost:9090/bookingms/booking/c5016ff0' | jq .
```


## Presentation slides

- [spring-cloud-presentation.pdf](spring-cloud-presentation.pdf)

## References

- [Cloud Native Java - by Kenny Bastani, Josh Long](https://www.oreilly.com/library/view/cloud-native-java/9781449374631/)
- [Spring Cloud Official Documentation](https://spring.io/projects/spring-cloud)
