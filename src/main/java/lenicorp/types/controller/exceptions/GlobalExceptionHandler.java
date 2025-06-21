package lenicorp.types.controller.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        exception.printStackTrace(); // journalise
        return Response.status(500).entity(
            new AppExceptionHandler.ErrorPayload(exception.getMessage())
        ).build();
    }
}
