package br.com.application;

import br.com.domain.model.Course;
import br.com.domain.service.CourseService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

@Path("/course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseController {
    
    @Inject
    CourseService service;

    @POST
    @Authenticated
    public Response create(Course course) {
        Course created = service.create(course);
        return Response.ok().entity(created.toEntity()).build();
    }

    @PUT
    @Authenticated
    public Response update(Course course) {
        Course updated = service.update(course);
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
        Course obtained = service.findById(id);
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
    @Path("/{courseId}/semesters/{semesterId}")
    @Authenticated
    public Response addSemesterToCourse(@RestPath Long courseId, @RestPath Long semesterId) {
        Course updated = service.addSemesterToCourse(courseId, semesterId);
        return Response.ok().entity(updated).build();
    }

    @DELETE
    @Path("/{courseId}/semesters/{semesterId}")
    @Authenticated
    public Response removeSemesterFromCourse(@RestPath Long courseId, @RestPath Long semesterId) {
        Course updated = service.removeSemesterFromCourse(courseId, semesterId);
        return Response.ok().entity(updated).build();
    }
}
