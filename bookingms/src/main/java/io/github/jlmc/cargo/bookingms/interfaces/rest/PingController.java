package io.github.jlmc.cargo.bookingms.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@RestController

@RefreshScope
public class PingController {

    //@formatter:off
    private static final Logger LOGGER = LoggerFactory.getLogger(PingController.class);
    private static final AtomicLong COUNTER = new AtomicLong(0L);
    public record PingStatus(String message, Instant creationOn, Instant time, Long refreshes) { }
    //@formatter:on

    private Instant creationOn;

    @Value("${booking.message:'Not Configured'}")
    String message;

    @RequestMapping(
            method = {
                    RequestMethod.GET,
                    RequestMethod.POST,
                    RequestMethod.PATCH,
                    RequestMethod.PUT,
                    RequestMethod.DELETE},
            path = "ping",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PingStatus pingStatus() {
        return new PingStatus(
                message,
                creationOn,
                Instant.now(),
                COUNTER.longValue());
    }

    @PostConstruct
    void initialize() {
        this.creationOn = Instant.now();
        LOGGER.info("Recreating the RefreshScope beans <{}> !!!", creationOn);
    }

    @EventListener
    public void refreshListener(RefreshScopeRefreshedEvent event) {
        // NOTE: this method is not required.
        LOGGER.info("The refresh count is now at <{}>", this.COUNTER.incrementAndGet());
    }
}
