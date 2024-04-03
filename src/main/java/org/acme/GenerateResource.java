package org.acme;

import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;

@Path("/generate")
@ApplicationScoped
public class GenerateResource {

    @Inject
    UriInfo uriInfo;

    @Channel("requests")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
    MutinyEmitter<URI> emitter;

    @GET
    public void generate(@RestQuery String url, @RestQuery long count) {
        System.out.printf("Generating %d requests to %s%n", count, url);
        URI callURI = uriInfo.getBaseUriBuilder().path(url).build();

        for (long i = 0; i < count; i++) {
            emitter.sendAndForget(callURI);
        }
    }

    @Incoming("requests")
    @RunOnVirtualThread
    public void sendRequest(URI uri) {
        try (Client client = ClientBuilder.newClient()) {
            client.target(uri).request().get();
        }
    }
}
