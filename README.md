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


# Api requests

# booking ms


```shell
curl -L -m 500 -X POST 'localhost:8081/booking' \
-H 'Content-Type: application/json' \
--data-raw '{
  "bookingAmount": 12,
  "originLocation": "ABC",
  "destLocation": "DEF",
  "destArrivalDeadline": "2022-04-01T21:23:43.598697Z"
}' | jq .
```


```shell
curl -L -m 500 -X GET 'localhost:8081/booking/c5016ff0' | jq .
```

```shell
curl -L -m 500 -X POST 'localhost:8081/booking/c5016ff0/route' | jq .
```


## Presentation

- https://docs.google.com/presentation/d/1iyRaIIBAbdKFpFKbG1igTN78z19B1Dsn9af0hUVl7Co/edit#slide=id.g11e8f9b583c_0_1599
