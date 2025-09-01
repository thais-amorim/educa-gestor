package br.com.application;

import br.com.domain.model.Subject;
import br.com.domain.service.SubjectService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

@Path("/subject")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubjectController {
    @Inject
    SubjectService service;

    @POST
    @Authenticated
    public Response create(Subject subject) {
        service.create(subject);
        return Response.ok().entity(subject.toEntity()).build();
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response delete(@RestPath Long id) {
        service.delete(id);
        return Response.ok().entity(id).build();
    }

    @GET
    @Path("/{id}")
    @Authenticated
    public Response findById(@RestPath Long id) {
        Subject obtained = service.findById(id);
        return Response.ok().entity(obtained.toEntity()).build();
    }

    @GET
    @Path("/all")
    public Response findAll() {
        return Response
                .ok(service.findAll())
                .build();
    }
}
