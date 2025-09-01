package br.com.application;

import br.com.domain.model.Semester;
import br.com.domain.service.SemesterService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

@Path("/semester")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SemesterController {
    
    @Inject
    SemesterService service;

    @POST
    @Authenticated
    public Response create(Semester semester) {
        Semester created = service.create(semester);
        return Response.ok().entity(created.toEntity()).build();
    }

    @PUT
    @Authenticated
    public Response update(Semester semester) {
        Semester updated = service.update(semester);
        return Response.ok().entity(updated.toEntity()).build();
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
        Semester obtained = service.findById(id);
        return Response.ok().entity(obtained).build();
    }

    @GET
    @Path("/all")
    public Response findAll() {
        return Response
                .ok(service.findAll())
                .build();
    }

    @POST
    @Path("/{semesterId}/subjects/{subjectId}")
    @Authenticated
    public Response addSubjectToSemester(@RestPath Long semesterId, @RestPath Long subjectId) {
        Semester updated = service.addSubjectToSemester(semesterId, subjectId);
        return Response.ok().entity(updated).build();
    }

    @DELETE
    @Path("/{semesterId}/subjects/{subjectId}")
    @Authenticated
    public Response removeSubjectFromSemester(@RestPath Long semesterId, @RestPath Long subjectId) {
        Semester updated = service.removeSubjectFromSemester(semesterId, subjectId);
        return Response.ok().entity(updated).build();
    }
}
