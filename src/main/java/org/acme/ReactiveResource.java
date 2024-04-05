package org.acme;

import io.smallrye.common.annotation.NonBlocking;
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
    public Uni<String> call() {
        concurrencyTracker.incAsync();
        System.out.println("Calling example API on " + Thread.currentThread().getName());

        return exampleClient.reactiveGet()
            .map(String::toUpperCase)
            .invoke(() -> concurrencyTracker.decAsync());
    }

    @GET
    @Path("/thread")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> thread() {
        return Uni.createFrom().item("Runnining on " + Thread.currentThread().getName());
    }

    @GET
    @Path("/thread2")
    @Produces(MediaType.TEXT_PLAIN)
    @NonBlocking
    public String thread2() {
        return "Running on " + Thread.currentThread().getName();
    }

}
