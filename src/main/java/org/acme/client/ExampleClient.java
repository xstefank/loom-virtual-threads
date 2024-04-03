package org.acme.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://example.com")
@Path("/")
public interface ExampleClient {

    @GET
    Uni<String> reactiveGet();

    @GET
    String blockingGet();
}
