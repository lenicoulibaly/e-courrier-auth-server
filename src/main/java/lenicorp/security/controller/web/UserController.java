package lenicorp.security.controller.web;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lenicorp.security.controller.services.specs.IUserService;
import lenicorp.security.model.dtos.AuthResponse;
import lenicorp.security.model.dtos.CreateUserDTO;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.validatorgroups.*;
import lombok.RequiredArgsConstructor;

@Path("/users")
@RequiredArgsConstructor
public class UserController
{
    private final IUserService userService;

    @POST
    @Path("/open/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthResponse login(@Valid @ConvertGroup(to = LoginGroup.class) UserDTO user)
    {
        return userService.login(user);
    }

    @GET
    @Path("/refresh-token/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthResponse refreshToken(@PathParam("userId") Long userId)
    {
        return userService.refreshToken(userId);
    }


    @POST
    @Path("/create")
    @RolesAllowed("CRT_USR")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO createUser(@Valid @ConvertGroup(to = CreateGroup.class) UserDTO user)
    {
        return userService.createUser(user);
    }

    @POST
    @Path("/create-with-profile")
    @RolesAllowed("CRT_USR")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO createUserWithProfile(@Valid @ConvertGroup(to = CreateGroup.class) CreateUserDTO user)
    {
        return userService.createUserWithProfile(user);
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO updateUser(@Valid @ConvertGroup(to = UpdateGroup.class) UserDTO user)
    {
        return userService.updateUser(user);
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void changePassword(@Valid @ConvertGroup(to = ChangePasswordGroup.class) UserDTO user)
    {
        userService.changePassword(user);
    }

    @PUT
    @Path("/open/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void resetPassword(@Valid @ConvertGroup(to = ResetPasswordGroup.class) UserDTO user)
    {
        userService.resetPassword(user);
    }

    @GET
    @Path("/send-activation-email/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void envoyerEmailActivation(@PathParam("userId") Long userId)
    {
        userService.sendActivationEmail(userId);
    }

    @PUT
    @Path("/block/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void blockUser(@PathParam("userId") Long userId)
    {
        userService.blockUser(userId);
    }

    @PUT
    @Path("/unblock/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void unblockUser(@PathParam("userId") Long userId)
    {
        userService.unblockUser(userId);
    }

    @PUT
    @Path("/activate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void activateAccount(@Valid @ConvertGroup(to = ActivateAccountGroup.class) UserDTO user)
    {
        userService.activateAccount(user);
    }

    @GET
    @Path("/send-reset-password-email/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void envoyerEmailReinitialisation(@PathParam("userId") Long userId)
    {
        userService.sendResetPasswordEmail(userId);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<UserDTO> searchUsers(@QueryParam("key") String key, @QueryParam("page") int page, @QueryParam("size") @DefaultValue("10") int size)
    {
        return userService.searchUsers(key, PageRequest.of(page, size));
    }
}
