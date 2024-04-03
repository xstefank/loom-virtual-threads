package org.acme;

import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;
import java.util.concurrent.ExecutorService;

@Path("/generate")
@ApplicationScoped
public class GenerateResource {

    @Inject
    UriInfo uriInfo;

    @VirtualThreads
    ExecutorService virtualThreadsExecutor;

    @GET
    public void generate(@RestQuery String url, @RestQuery long count) {
        System.out.printf("Generating %d requests to %s...%n", count, url);
        URI callURI = uriInfo.getBaseUriBuilder().path(url).build();

        for (long i = 0; i < count; i++) {
            virtualThreadsExecutor.submit(() -> sendRequest(callURI));
        }
    }

    public void sendRequest(URI uri) {
        try (Client client = ClientBuilder.newClient()) {
            client.target(uri).request().get();
        }
    }
}
