## Bootstrap example

1. start the services:
```shell
docker-compose up rabbitmq postgres zipkin -d
```

2. start *Configuration server*:

3. start *Service Discovery server*:

4. start the **routhingms**:

5 start the **bookingms**:



## Testing

Now instead o call directly the downstream microservices we can call the api-gateway, for example:

```shell
http://{API-GATEWAY-HOST}:{API-GATEWAY-PORT}/{DOWNSTREAM-SERVICE-ID}/booking/ef622f7a/route
```

### so we can run our example

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


```
curl -L -m 500 -X POST 'localhost:9090/bookingms/booking/ef622f7a/route'
```
