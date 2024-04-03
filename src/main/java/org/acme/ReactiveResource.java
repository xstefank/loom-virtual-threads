package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.client.ExampleClient;
import org.acme.concurrency.ConcurrencyTracker;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/reactive")
@ApplicationScoped
public class ReactiveResource {

    @Inject
    ConcurrencyTracker concurrencyTracker;

    @RestClient
    ExampleClient exampleClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        concurrencyTracker.incAsync();
        System.out.println("Calling example API...");

        return exampleClient.reactiveGet()
            .onItem().transformToUni((s, uniEmitter) -> {
                concurrencyTracker.decAsync();
                uniEmitter.complete(s.toUpperCase());
            });
    }
}
