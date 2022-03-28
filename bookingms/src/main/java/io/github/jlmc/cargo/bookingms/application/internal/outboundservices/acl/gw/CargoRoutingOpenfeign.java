package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw;

import io.github.jlmc.cargo.bookingms.shareddomain.TransitPath;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "api-gateway", // application-name of the api-gateway, value = "routingms",
        contextId="routingmsOpenfeign",
       // configuration = FeignClientConfig.class,
        decode404 = true)
public interface CargoRoutingOpenfeign {

    //private static final String ROUTING_MS = "http://routingms/routing";

    @GetMapping(path = "/routingms/routing", produces = MediaType.APPLICATION_JSON_VALUE)
    TransitPath findOptimalRoute(
            @RequestParam(value = "origin", required = false) String originUnLocode,
            @RequestParam(value = "destination", required = false) String destinationUnLocode,
            @RequestParam(value = "deadline", required = false) String deadline
    );
}
