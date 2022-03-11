package org.acme;
//import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.acme.organization.Organization;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import static javax.ws.rs.core.Response.ok;

@Path("/organisations/register-organisation")
public class OrganisationResource {
    @Inject
    PgPool client;
    @POST
    public Uni<Response> createOrg(Organization organization) throws SQLException {

        return Organization.create(client,organization)
                .onItem()
                .transform(id -> URI.create("/organisation/" + id))
                .onItem()
                .transform(uri -> Response.created(uri).build());

//



    }
}
