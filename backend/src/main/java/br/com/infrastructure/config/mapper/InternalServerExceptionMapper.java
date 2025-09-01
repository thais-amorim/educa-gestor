package br.com.infrastructure.config.mapper;

import br.com.application.response.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * ExceptionMapper específico para tratar erros internos do servidor (HTTP 500).
 * Este mapper tem prioridade sobre o GlobalExceptionMapper para exceções específicas
 * que devem resultar em erro 500.
 */
@Provider
public class InternalServerExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(InternalServerExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.error("Internal server error occurred: ", exception);

        ErrorResponse errorResponse = new ErrorResponse(
                getInternalErrorMessage(exception),
                LocalDateTime.now(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String getInternalErrorMessage(Throwable exception) {
        switch (exception) {
            case SQLException sqlException -> {
                return "Database error occurred. Please try again later.";
            }
            case null, default -> {
                return "An internal server error occurred. Please try again later.";
            }
        }
    }
}
