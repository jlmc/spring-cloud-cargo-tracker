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
