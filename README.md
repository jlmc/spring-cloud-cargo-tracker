#  Spring cloud cargo tracker

**Note**: In almost every example we will need always some docker service, so, before you start and to prevent any unexpected problem, it is a good idea startup the following docker-compose services: 

```shell
docker-compose up rabbitmq postgres zipkin -d
```

# services

- [RabbitMQ](http://localhost:15672/#/exchanges)


# build

```shell
mvn clean package
```

## 1. start up rotting-ms
```shell
java -jar routingms/target/routingms-0.0.1-SNAPSHOT.jar
```

## 2. start up booking-ms
```shell
java -jar bookingms/target/bookingms-0.0.1-SNAPSHOT.jar
```
