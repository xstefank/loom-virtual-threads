package org.acme;

import io.smallrye.common.annotation.RunOnVirtualThread;
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

import java.util.concurrent.locks.ReentrantLock;

@Path("/problem")
@ApplicationScoped
public class ProblemsResource {

    @Inject
    ConcurrencyTracker concurrencyTracker;

    @RestClient
    ExampleClient exampleClient;

    @GET
    @Path("/reactive")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> call() {
        concurrencyTracker.incAsync();
        System.out.println("Calling example API on " + Thread.currentThread().getName());

//        pinTheCarrierThread();

        return exampleClient.reactiveGet()
            .map(String::toUpperCase)
            .invoke(() -> concurrencyTracker.decAsync());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/virtual")
    @RunOnVirtualThread
    public String callVirtual() {
        concurrencyTracker.inc();
        try {
            System.out.println("Calling example API on " + Thread.currentThread().getName());

            pinTheCarrierThread();
//            pinTheCarrierThreadLock();

            return exampleClient.blockingGet().toUpperCase();
        } finally {
            concurrencyTracker.dec();
        }
    }

    private void pinTheCarrierThread() {
        synchronized (this) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
                // For testing purpose only.
            }
        }
    }

    private ReentrantLock lock = new ReentrantLock();

    private void pinTheCarrierThreadLock() {
        lock.lock();
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {

        } finally {
            lock.unlock();
        }
    }
}
