package io.github.jlmc.cargo.bookingms.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController

public class PingController {

    //@formatter:off
    public record PingStatus(String message, Instant time) { }
    //@formatter:on

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
        return new PingStatus(message, Instant.now());
    }
}
