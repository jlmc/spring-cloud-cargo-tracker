1. dependency
```
org.springframework.cloud:spring-cloud-starter-loadbalancer
```


2. create a configuration
```java
@LoadBalancerClient(value = "routingms") // must be the value of the target FeignClient value
class CargoRoutingOpenfeignConfig {

    @Bean
    @LoadBalancer
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
```

3. apply configuration

```java
@FeignClient(
        value = "routingms",
        contextId="routingmsOpenfeign",
        configuration = CargoRoutingOpenfeignConfig.class,
        decode404 = true)
public interface CargoRoutingOpenfeign {

    //private static final String ROUTING_MS = "http://routingms/routing";

    @GetMapping(path = "routing", produces = MediaType.APPLICATION_JSON_VALUE)
    TransitPath findOptimalRoute(
            @RequestParam(value = "origin", required = false) String originUnLocode,
            @RequestParam(value = "destination", required = false) String destinationUnLocode,
            @RequestParam(value = "deadline", required = false) String deadline
    );
}
```


---

```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }

```
