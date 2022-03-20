package io.github.jlmc.cargo.bookingms.application.internal.outboundservices.acl.gw;

import feign.Feign;
import feign.RequestInterceptor;
import io.github.jlmc.cargo.bookingms.util.RequestHeadersContext;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;

import static io.github.jlmc.cargo.bookingms.util.RequestHeadersContext.AUTHORIZATION;
import static io.github.jlmc.cargo.bookingms.util.RequestHeadersContext.X_SYS;

@LoadBalancerClients({
        @LoadBalancerClient(value = "routingms")
})
public class CargoRoutingOpenFeignLoadBalancerConfig {

    @Bean
    @LoadBalanced
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    /**
     * Append headers:
     * https://www.baeldung.com/spring-cloud-feign-oauth-token
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            RequestHeadersContext.authentication()
                                 .ifPresent(authentication -> template.header(AUTHORIZATION, authentication));
            RequestHeadersContext.xsys()
                                 .ifPresent(xsys -> template.header(X_SYS, xsys));
        };
    }
}
