package org.acme;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.mutiny.pgclient.PgPool;
import org.acme.retailplace.RetailPlace;
import org.acme.retailplace.RetailPlaceAddress;
import org.acme.retailplace.RetailPlaceResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/retailplace")
public class RetailPlaceResource {
    @Inject
    PgPool client;
    @Path("/create")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(RetailPlace retailPlace) {
        return RetailPlace.create(client, retailPlace).onItem().transform(id -> Response.ok(id).status(CREATED).build());
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Tuple2<RetailPlace, RetailPlaceAddress>> get(@PathParam("id") long id){
        return Uni.combine().all().unis(RetailPlace.get(client, id), RetailPlaceAddress.get(client, id)).asTuple();
        //return RetailPlace.get(client, id);
    }
}
