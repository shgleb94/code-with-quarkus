package org.acme;
//import io.smallrye.mutiny.Uni;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.acme.organization.LegalAddress;
import org.acme.organization.Organization;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestSseElementType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import static javax.ws.rs.core.Response.ok;
import static org.jboss.resteasy.reactive.RestResponse.Status.CREATED;

@Path("/organisations")
public class OrganisationResource {
    @Inject
    PgPool client;
    @Path("/register-organisation")
    @POST
    public Uni<Response> createOrg(Organization organization) throws SQLException {
        if (organization == null || organization.getId() != 0) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }


        return Organization.create(client, organization)
                .onItem()
                .transform(id -> URI.create("/organisations" + organization.getId()))
                .onItem()
                .transform(uri  -> Response.ok(organization).status(CREATED).build());
    }

    @Path("/change-organisation")
    @POST
    public Uni<Response> ChangeOrg(Organization organization) throws SQLException {



        return Organization.changeOrg(client, organization)
                .onItem()
                .transform(id -> URI.create("/organisations" + organization.getId()))
                .onItem()
                .transform(uri  -> Response.ok(organization).status(CREATED).build());
    }

    @POST
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Uni<Response> getOrg(Organization organization) {
        return Organization.checkOrganization(client, organization).onItem().transform(organization1 -> organization1 != null ? Response.ok(organization1) : Response.status(RestResponse.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    //
}
