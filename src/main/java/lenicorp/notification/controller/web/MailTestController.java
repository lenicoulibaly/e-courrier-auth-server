package lenicorp.notification.controller.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import lenicorp.notification.controller.services.MailServiceInterface;
import lenicorp.notification.model.dto.MailRequest;

import java.util.concurrent.CompletableFuture;

@Path("/open/mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MailTestController
{
    @Inject
    MailServiceInterface mailService;

    @POST
    @Path("/send")
    public CompletableFuture<Response> sendMail(@Valid MailRequest mailRequest)
    {
        return mailService.sendMailAsync(mailRequest).thenApply(response ->
        {
            if (response.isSuccess()) return Response.ok(response).build();
            else  return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(response)
                        .build();
        });
    }
}