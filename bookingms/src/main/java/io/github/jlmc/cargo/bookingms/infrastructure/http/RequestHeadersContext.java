package io.github.jlmc.cargo.bookingms.infrastructure.http;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.http.HttpHeaders;

import java.util.Collection;
import java.util.Optional;

public final class RequestHeadersContext {

    //@formatter:off
    public static final String X_SYS = "X-SYS";
    public static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
    private static final NamedInheritableThreadLocal<HttpHeaders> HEADERS =
            new NamedInheritableThreadLocal<>("HEADERS");
    //@formatter:on

    public static void set(HttpHeaders headers) {
        HEADERS.set(headers);
    }

    public static Optional<String> headerValue(String headerName) {
        return Optional.ofNullable(HEADERS.get())
                       .map(hs -> hs.get(headerName))
                       .stream()
                       .flatMap(Collection::stream)
                       .findFirst();
    }

    public static String headerValueOrElse(String headerName, String defaultValue) {
        return headerValue(headerName)
                .orElse(defaultValue);
    }

    public static Optional<String> authentication() {
        return headerValue(AUTHORIZATION);
    }

    public static Optional<String> xsys() {
        return headerValue(X_SYS);
    }
}
