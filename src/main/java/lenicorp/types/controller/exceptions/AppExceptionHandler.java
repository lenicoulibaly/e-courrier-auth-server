package lenicorp.types.controller.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AppExceptionHandler implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessages())
                .build();
    }

    public static class ErrorPayload {
        public String error;

        public ErrorPayload(String error) {
            this.error = error;
        }
    }
}
