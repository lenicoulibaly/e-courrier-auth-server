package lenicorp.security.controller.web;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lenicorp.notification.model.dto.MailResponse;
import lenicorp.security.controller.services.IUserService;
import lombok.RequiredArgsConstructor;

@Path("/open/users")
@RequiredArgsConstructor
public class UserController
{
    private final IUserService userService;

    @GET
    @Path("/send-activation-email/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void envoyerEmailActivation(@PathParam("userId") Long userId)
    {
        userService.sendActivationEmail(userId);
    }

    @GET
    @Path("/send-reset-password-email/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void envoyerEmailReinitialisation(@PathParam("userId") Long userId)
    {
        userService.sendResetPasswordEmail(userId);
    }

    private static Response buildEmailResponse(MailResponse response)
    {
        if (response.isSuccess()) return Response.ok(response).build();
        else return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response.getErrorMessage())
                .build();
    }
}
